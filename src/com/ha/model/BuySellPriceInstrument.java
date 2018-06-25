package com.ha.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;


@Entity(name = "BuySellPriceInstrument")
@DiscriminatorValue(value = "B")
public class BuySellPriceInstrument extends Instrument {
	@OneToMany(mappedBy = "instrument", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Quote> buyQuotes = new HashSet<>();

	@OneToMany(mappedBy = "instrument", cascade = CascadeType.ALL, orphanRemoval = true)
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
