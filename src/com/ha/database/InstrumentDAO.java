package com.ha.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.stream.Collectors;

import com.ha.model.BuySellPriceInstrument;
import com.ha.model.Instrument;
import com.ha.model.Quote;
import com.ha.model.SinglePriceInstrument;
import com.ha.model.User;

public class InstrumentDAO extends HADAO<Instrument> {
	private User user;
	
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	User getUser() {
		return user;
	}

	@Override
	String getTableName() {
		return "HA.INSTRUMENT";
	}

	@Override
	String getValuesString(Instrument instrument) {
		return
				user.getId()+",'"+
				instrument.getName()+"','"+
				instrument.getCode()+"','"+
				instrument.getType()+"'";
	}

	@Override
	String getColumnNames() {
		return "USER_ID,NAME,CODE,TYPE";
	}

	@Override
	String getColumnValuePairs(Instrument instrument) {
		return
				"USER_ID='"+user.getId()+
				"NAME='"+instrument.getName()+
				"',CODE='"+instrument.getCode()+
				"',TYPE='"+instrument.getType()+"'";
	}

	@Override
	Instrument createModelObject(ResultSet resultSet) throws SQLException {
		Instrument instrument = Instrument.getInstrument(resultSet.getString("type").charAt(0));
		instrument.setId(resultSet.getInt("ID"));
		instrument.setName(resultSet.getString("NAME"));
		instrument.setCode(resultSet.getString("CODE"));
		QuoteDAO quoteDAO = new QuoteDAO();
		quoteDAO.setDbResources(dbResources);
		Set<Quote> quotes = quoteDAO.selectQuotesByInstrumentId(instrument.getId());
		if (instrument.isSinglePrice()) {
			for(Quote quote : quotes) {
				((SinglePriceInstrument) instrument).addQuote(quote);
			}
		} else { // Instrument has buy and sell price
			for(Quote quote : quotes.stream().filter(p -> p.getType() == ('B')).collect(Collectors.toSet())) {
				((BuySellPriceInstrument) instrument).addBuyQuote(quote);
			}
			for(Quote quote : quotes.stream().filter(p -> p.getType() == ('S')).collect(Collectors.toSet())) {
				((BuySellPriceInstrument) instrument).addSellQuote(quote);
			}
		}
		return instrument;
	}

}
