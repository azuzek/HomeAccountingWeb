package com.ha.model;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity(name = "ExpenseAccount")
@DiscriminatorValue(value = "E")
public class ExpenseAccount extends Account {
public char getType () {
	return 'E';
}

@Override
public void debit(BigDecimal amount) {
	setBalance(getBalance().add(amount));
}

@Override
public void credit(BigDecimal amount) {
	setBalance(getBalance().subtract(amount));
}
}
