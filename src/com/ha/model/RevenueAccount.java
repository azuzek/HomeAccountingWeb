package com.ha.model;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "RevenueAccount")
@DiscriminatorValue(value = "R")
public class RevenueAccount extends Account {
	public char getType() {
		return 'R';
	}
	
	@Override
	public void debit(BigDecimal amount) {
		setBalance(getBalance().subtract(amount));
	}
	
	@Override
	public void credit(BigDecimal amount) {
		setBalance(getBalance().add(amount));
	}
}
