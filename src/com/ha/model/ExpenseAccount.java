package com.ha.model;

import java.math.BigDecimal;

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
