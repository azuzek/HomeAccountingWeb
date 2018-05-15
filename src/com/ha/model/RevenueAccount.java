package com.ha.model;

import java.math.BigDecimal;

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
