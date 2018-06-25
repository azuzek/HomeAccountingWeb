package com.ha.model;

import java.math.BigDecimal;

import javax.persistence.Embeddable;

@Embeddable
public class LocalCurrencyAmount extends Amount {
	BigDecimal total;

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	@Override
	public boolean isLocalCurrencyAmount() {
		return true;
	}

	@Override
	public boolean isCalculatedAmount() {
		return false;
	}

}
