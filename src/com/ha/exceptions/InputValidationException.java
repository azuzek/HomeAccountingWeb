package com.ha.exceptions;

public class InputValidationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fieldName;
	private String inputString;
	
	public InputValidationException(String field, String value, String message) {
		super(message);
		fieldName = field;
		inputString = value;
	}
	
	public String getFieldName() {
		return fieldName;
	}

	public String getInputString() {
		return inputString;
	}
}
