package com.ha.model;

import java.util.HashSet;
import java.util.Set;

public class BuySellPriceInstrument extends Instrument {
	private Set<Quote> buyQuotes = new HashSet<>();
	private Set<Quote> sellQuotes = new HashSet<>();
	
	public void addBuyQuote(Quote buyQuote) {
		buyQuote.setInstrument(this);
		buyQuote.setType('B');
		this.buyQuotes.add(buyQuote);
	}
	
	public Set<Quote> getBuyQuotes() {
		return this.buyQuotes;
	}
	
	public void addSellQuote (Quote sellQuote) {
		sellQuote.setInstrument(this);
		sellQuote.setType('S');
		this.sellQuotes.add(sellQuote);
	}
	
	public Set<Quote> getSellQuotes() {
		return this.sellQuotes;
	}

	@Override
	public char getType() {
		return 'D';
	}
}
