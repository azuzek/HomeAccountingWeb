package com.ha.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class AccountTypeConverter implements AttributeConverter<AccountType, Character> {
	public Character convertToDatabaseColumn( AccountType value ) {
	    if ( value == null ) {
	        return null;
	    }
	    return value.getCode();
    }

    public AccountType convertToEntityAttribute( Character value ) {
        if ( value == null ) {
            return null;
        }
        return AccountType.fromCode( value );
    }
}
