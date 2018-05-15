package com.ha.model;

public enum AccountType {
		ASSET ('A'),
		LIABILITY ('L'),
		REVENUE ('R'),
		EXPENSE ('E'),
		EQUITY ('Q');
	
	private final char code;
	AccountType(char code) {
		this.code = code;
	}
	public char getCode() {
		return code;
	}
	public static AccountType fromCode(char code) {
		switch (code) {
		case 'A': return ASSET;
		case 'L': return LIABILITY;
		case 'R': return REVENUE;
		case 'E': return EXPENSE;
		case 'Q': return EQUITY;
		default: return null;
		}
	}
}
