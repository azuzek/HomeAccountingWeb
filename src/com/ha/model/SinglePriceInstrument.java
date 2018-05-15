package com.ha.model;

import java.util.HashSet;
import java.util.Set;

public class SinglePriceInstrument extends Instrument {
	private Set<Quote> quotes = new HashSet<>();

	public void addQuote(Quote quote) {
		quote.setInstrument(this);
		quotes.add(quote);
	}
	
	public Set<Quote> getQuotes() {
		return quotes;
	}
	
	@Override
	public char getType() {
		return 'S';
	}
}
