package com.ha.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;


@Entity(name = "SinglePriceInstrument")
@DiscriminatorValue(value = "S")
public class SinglePriceInstrument extends Instrument {
	@OneToMany(mappedBy = "instrument", cascade = CascadeType.ALL, orphanRemoval = true)
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
