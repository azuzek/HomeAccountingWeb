package com.ha.model;

public abstract class Instrument extends UserFilteredObject {
	private String code;
	private String name;
	
	public static Instrument getInstrument(char type) {
		Instrument instrument = null;
		switch(type) {
		case 'S': instrument = new SinglePriceInstrument();
			  break;
		case 'D': instrument = new BuySellPriceInstrument();
			  break;
		default: instrument = null;
		}
		return instrument;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	abstract public char getType();
	
	public boolean isSinglePrice() {
		return this.getCode().equals('S');
	}
	
	public boolean isBuySellPrice() {
		return this.getCode().equals('D');
	}
}
