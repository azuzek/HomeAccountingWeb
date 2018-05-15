package com.ha.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import com.ha.model.Account;
import com.ha.model.User;

public class AccountDAO extends HADAO<Account> {
	private User user;
	
	public void setUser(User user) {
		this.user = user;
	}
	
	User getUser() {
		return user;
	}
	
	String getTableName() {
		return "HA.ACCOUNT";
	}
	
	public Set<Account> selectModelObjects() {
		ResultSet resultSet = null;
		Account account;
		String query;
		Set<Account> accounts = super.selectModelObjects();
		try {
			query = 
				"select id, count(entry_line_id) as debit_frequency from ("+
				"	select account.id, account.user_id, account.name, account.type, account.balance,debit_entry_line.id as entry_line_id "+
				"	from account left join ("+
				"		select id,user_id,entry_id,account_id,action,amount from entry_line where entry_line.action='D'"+
				"	) as debit_entry_line "+
				"	on account.id=debit_entry_line.account_id"+
				") as entry_related "+
				"where user_id="+getUser().getId()+
				" group by id "+
				"order by debit_frequency desc";
			resultSet = dbResources.executeStatement(query);
			while (resultSet.next()) {
				int id = resultSet.getInt("ID");
				account = accounts.stream().filter(p -> p.getId()==id).findFirst().get();
				account.setDebitFrequenty(resultSet.getInt("DEBIT_FREQUENCY"));
			}
			query =
				"select id, count(entry_line_id) as credit_frequency from ("+
				"	select account.id, account.user_id, account.name, account.type, account.balance,debit_entry_line.id as entry_line_id "+
				"	from account left join ("+
				"		select id,user_id,entry_id,account_id,action,amount from entry_line where entry_line.action='E'"+
				"	) as debit_entry_line "+
				"	on account.id=debit_entry_line.account_id"+
				") as entry_related "+
				"where user_id="+getUser().getId()+
				" group by id "+
				"order by credit_frequency desc";
			resultSet = dbResources.executeStatement(query);
			while (resultSet.next()) {
				int id = resultSet.getInt("ID");
				account = accounts.stream().filter(p -> p.getId()==id).findFirst().get();
				account.setCreditFrequency(resultSet.getInt("CREDIT_FREQUENCY"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return accounts;
	}
	String getValuesString(Account account) {
		String valuesString = 
				user.getId()+","+
				account.getCategoryId()+",'"+
				account.getName()+"','"+
				account.getType()+"',"+
				account.getBalance()+","+
				(account.isActive()? 1: 0)+","+
				(account.isLocalCurrency()? 1: 0);
		if(account.isLocalCurrency()) {
			valuesString = valuesString+",null";
		} else {
			valuesString = valuesString+","+account.getInstrument().getId();
		}
		return valuesString;
	}
	String getColumnNames() {
		return "USER_ID,CATEGORY_ID,NAME,TYPE,BALANCE,ACTIVE,LOCAL_CURRENCY,INSTRUMENT_ID";
	}
	
	Account createModelObject(ResultSet resultSet) throws SQLException {
		Account account = null;
		account = Account.getAccount(resultSet.getString("type").charAt(0));
		account.setId(resultSet.getInt("ID"));
		account.setCategoryId(resultSet.getInt("CATEGORY_ID"));
		account.setName(resultSet.getString("name"));
		account.setBalance(resultSet.getBigDecimal("balance"));
		account.setActive(resultSet.getBoolean("ACTIVE"));
		account.setLocalCurrency(resultSet.getBoolean("LOCAL_CURRENCY"));
		if(!account.isLocalCurrency()) {
			InstrumentDAO instrumentDAO = new InstrumentDAO();
			DBResources instDbResources = new DBResources();
			instDbResources.setHaDataSource(this.getDbResources().getHaDataSource());
			instrumentDAO.setDbResources(instDbResources);
			instrumentDAO.setUser(user);
			account.setInstrument(instrumentDAO.findModelObject(resultSet.getInt("INSTRUMENT_ID")));
		}
		return account;
	}

	@Override
	String getColumnValuePairs(Account account) {
		String columnValuePairs =
				"USER_ID='"+user.getId()+
				"',CATEGORY_ID='"+account.getCategoryId()+
				"',NAME='"+account.getName()+
				"',TYPE='"+account.getType()+
				"',BALANCE="+account.getBalance()+
				",ACTIVE="+(account.isActive()? 1: 0)+
				",LOCAL_CURRENCY="+(account.isLocalCurrency()? 1: 0);
		if(account.isLocalCurrency()) {
			columnValuePairs = columnValuePairs+",INSTRUMENT_ID=null";
		} else {
			columnValuePairs = columnValuePairs+",INSTRUMENT_ID="+account.getInstrument().getId();
		}
		return columnValuePairs;
	}
}
