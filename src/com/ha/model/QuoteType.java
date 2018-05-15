package com.ha.model;

public enum QuoteType {
	BUY ('B'),
	SELL ('S');

	private final char code;
	
	QuoteType(char code) {
		this.code = code;
	}
	public char getCode() {
		return code;
	}
	public static QuoteType fromCode(char code) {
		switch (code) {
		case 'B': return BUY;
		case 'S': return SELL;
		default: return null;
		}
	}
}
