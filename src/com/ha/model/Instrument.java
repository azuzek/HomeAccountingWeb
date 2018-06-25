package com.ha.model;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "INSTRUMENT")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="TYPE", discriminatorType=DiscriminatorType.CHAR)
@DiscriminatorValue(value = "Z")
public abstract class Instrument extends ModelObject {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String code;
	private String name;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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
