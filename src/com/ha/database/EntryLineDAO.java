package com.ha.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.ha.model.EntryLine;
import com.ha.model.LocalCurrencyAmount;
import com.ha.model.CalculatedAmount;
import com.ha.model.Quote;
import com.ha.model.User;

public class EntryLineDAO extends HADAO<EntryLine> {
	private User user;

	@Override
	User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	String getTableName() {
		return "HA.ENTRY_LINE";
	}

	@Override
	String getValuesString(EntryLine entryLine) {
		String valuesString = 
				entryLine.getUserId()+","+
				entryLine.getEntryId()+","+
				entryLine.getAccountId()+",'"+
				entryLine.getAction()+"',";
		if (entryLine.getAmount().isLocalCurrencyAmount()) {
			valuesString = valuesString + entryLine.getAmount().getTotal()+",";
		} else {
			valuesString = valuesString + "null,";
		}
		if (entryLine.getAmount().isCalculatedAmount()) {
			valuesString = valuesString + 
					((CalculatedAmount) entryLine.getAmount()).getQuote().getId()+","+
					((CalculatedAmount) entryLine.getAmount()).getQuantity();
		} else {
			valuesString = valuesString + "null,null";
		}
		return valuesString;
	}

	@Override
	String getColumnNames() {
		return "USER_ID,ENTRY_ID,ACCOUNT_ID,ACTION,AMOUNT,QUOTE_ID,QUANTITY";
	}

	@Override
	EntryLine createModelObject(ResultSet resultSet) throws SQLException {
		EntryLine newEntryLine = EntryLine.getEntryLine(resultSet.getString("ACTION").charAt(0));
		newEntryLine.setUserId(resultSet.getInt("USER_ID"));
		newEntryLine.setEntryId(resultSet.getInt("ENTRY_ID"));
		newEntryLine.setAccountId(resultSet.getInt("ACCOUNT_ID"));
		if(resultSet.getBigDecimal("AMOUNT") != null) {
			LocalCurrencyAmount amount = new LocalCurrencyAmount();
			amount.setTotal(resultSet.getBigDecimal("AMOUNT"));
			newEntryLine.setAmount(amount);
		} else {
			CalculatedAmount amount = new CalculatedAmount();
			QuoteDAO quoteDAO = new QuoteDAO();
			quoteDAO.setDbResources(dbResources);
			Quote quote = quoteDAO.findModelObject(resultSet.getInt("QUOTE_ID"));
			amount.setQuote(quote);
			amount.setQuantity(resultSet.getBigDecimal("QUANTITY"));
			newEntryLine.setAmount(amount);
		}
		/*
		AccountDAO accountDAO = new AccountDAO();
		accountDAO.setDbResources(dbResources);
		newEntryLine.setAccount(accountDAO.findModelObject(newEntryLine.getAccountId()));
		*/
		return newEntryLine;
	}

	@Override
	String getColumnValuePairs(EntryLine entryLine) {
		String columnValuePairs = 
				"USER_ID="+entryLine.getUserId()+
				",ENTRY_ID="+entryLine.getEntryId()+
				",ACCOUNT_ID="+entryLine.getAccountId()+
				",ACTION='"+entryLine.getAction();
		if (entryLine.getAmount().isLocalCurrencyAmount()) {
			columnValuePairs = columnValuePairs + "',AMOUNT="+entryLine.getAmount().getTotal();
		} else {
			columnValuePairs = columnValuePairs + "',AMOUNT=NULL";
		}
		if (entryLine.getAmount().isCalculatedAmount()) {
			columnValuePairs = columnValuePairs +
					"',QUOTE_ID="+((CalculatedAmount) entryLine.getAmount()).getQuote().getId()+
					",QUANTITY="+((CalculatedAmount) entryLine.getAmount()).getQuantity();
		} else {
			columnValuePairs = columnValuePairs + "',QUOTE_ID=NUL,QUANTITY=NULL";
		}
		return columnValuePairs;
	}
	
	String getSelectByEntryIdQuery(int entryId) {
		return "SELECT * FROM "+getTableName()+" WHERE ENTRY_ID='"+entryId+"'";
	}
	
	public Set<EntryLine> selectEntryLinesByEntryId(int id) {
		ResultSet resultSet = null;
		EntryLine entryLine;
		Set<EntryLine> entryLines = new HashSet<>();
		String query = getSelectByEntryIdQuery(id);
		resultSet = dbResources.executeStatement(query);
		try {
			while (resultSet.next()) {
				entryLine = createModelObject(resultSet);
				entryLines.add(entryLine);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return entryLines;
	}
}
