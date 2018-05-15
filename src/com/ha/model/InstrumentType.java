package com.ha.model;

public enum InstrumentType {
	SINGLE_PRICE ('S'),
	BUY_SELL_PRICE ('D');

	private final char code;
	
	InstrumentType(char code) {
		this.code = code;
	}

	public char getCode() {
		return code;
	}
	
	public static InstrumentType fromCode(char code) {
		switch (code) {
		case 'S': return SINGLE_PRICE;
		case 'D': return BUY_SELL_PRICE;
		default: return null;
		}
	}
}
