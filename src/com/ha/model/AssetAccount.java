package com.ha.model;

import java.math.BigDecimal;

public class AssetAccount extends Account {
	public char getType() {
		return 'A';
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
