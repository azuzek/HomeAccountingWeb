package com.ha.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import com.ha.model.Quote;
import com.ha.model.User;

public class QuoteDAO extends HADAO<Quote> {
	private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

	@Override
	User getUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	String getTableName() {
		return "HA.QUOTE";
	}

	@Override
	String getValuesString(Quote quote) {
		return
				quote.getInstrument().getId()+",'"+
				quote.getType()+"','"+
				quote.getDate().format(FORMATTER)+"',"+
				quote.getPrice();
	}

	@Override
	String getColumnNames() {
		return "INSTRUMENT_ID,TYPE,DATE,PRICE";
	}

	@Override
	String getColumnValuePairs(Quote quote) {
		return
				"INSTRUMENT_ID="+quote.getInstrument().getId()+
				",TYPE="+((quote.getType() == 0)? "'"+quote.getType()+"'": "null")+
				",DATE='"+quote.getDate().format(FORMATTER)+
				"',PRICE="+quote.getPrice();
	}

	@Override
	Quote createModelObject(ResultSet resultSet) throws SQLException {
		Quote quote = new Quote();
		quote.setType(resultSet.getString("TYPE").charAt(0));
		quote.setDate(resultSet.getTimestamp("DATE").toLocalDateTime().toLocalDate());
		quote.setPrice(resultSet.getBigDecimal("PRICE"));
		return quote;
	}
	
	public Set<Quote> selectQuotesByInstrumentId(int id) {
		ResultSet resultSet = null;
		Quote quote;
		Set<Quote> quotes = new HashSet<>();
		String query = getSelectByInstrumentIdQuery(id);
		resultSet = dbResources.executeStatement(query);
		try {
			while (resultSet.next()) {
				quote = createModelObject(resultSet);
				quotes.add(quote);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return quotes;
	}

	String getSelectByInstrumentIdQuery(int instrumentId) {
		return "SELECT * FROM "+getTableName()+" WHERE INSTRUMENT_ID='"+instrumentId+"'";
	}
}
