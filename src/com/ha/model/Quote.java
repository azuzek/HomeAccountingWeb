package com.ha.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Quote extends ModelObject {
	private BigDecimal price;
	private LocalDate date;
	private Instrument instrument;
	private char type;
	
	public char getType() {
		return type;
	}
	public void setType(char type) {
		this.type = type;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public Instrument getInstrument() {
		return instrument;
	}
	public void setInstrument(Instrument instrument) {
		this.instrument = instrument;
	}
}
