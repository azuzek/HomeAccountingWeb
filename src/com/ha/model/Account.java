/**
 * 
 */
package com.ha.model;

import java.math.BigDecimal;

/**
 * @author MLA
 *
 */
public abstract class Account extends ModelObject {

private String name;
private int categoryId;
private BigDecimal balance;
private boolean active = true;
private boolean localCurrency = true;
private Instrument instrument;

public Instrument getInstrument() {
	return instrument;
}
public void setInstrument(Instrument instrument) {
	this.instrument = instrument;
}
public boolean isActive() {
	return active;
}
public void setActive(boolean active) {
	this.active = active;
}
private int debitFrequency = 0;
private int creditFrequency = 0;

public static Account getAccount(char type) {
	Account account = null;
	switch(type) {
	case 'A': account = new AssetAccount();
		  break;
	case 'E': account = new ExpenseAccount();
		  break;
	case 'L': account = new LiabilityAccount();
  		  break;
	case 'R': account = new RevenueAccount();
  		  break;
	default: account = null;
	}
	return account;
}
abstract public void debit(BigDecimal amount);
abstract public void credit(BigDecimal amount);
abstract public char getType();

/**
 * @return the name
 */
public String getName() {
	return name;
}

/**
 * @param name the name to set
 */
public void setName(String name) {
	this.name = name;
}

/**
 *  @return the category id
 */
public int getCategoryId() {
	return categoryId;
}

/**
 * @param categoryId the category id to set
 */
public void setCategoryId(int categoryId) {
	this.categoryId = categoryId;
}

/**
 * @return the balance
 */
public BigDecimal getBalance() {
	return balance;
}
public void setBalance(BigDecimal balance) {
	this.balance = balance;
}
public boolean isLocalCurrency() {
	return localCurrency;
}
public void setLocalCurrency(Boolean localCurrency) {
	this.localCurrency = localCurrency;
}
public int getCreditFrequency() {
	return creditFrequency;
}
public void setCreditFrequency(int frequency) {
	creditFrequency = frequency;
}
public int getDebitFrequency() {
	return debitFrequency;
}
public void setDebitFrequenty(int frequencty) {
	debitFrequency = frequencty;
}
}
