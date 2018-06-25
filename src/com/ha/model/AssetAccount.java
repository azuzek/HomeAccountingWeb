package com.ha.model;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "AssetAccount")
@DiscriminatorValue(value = "A")
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
