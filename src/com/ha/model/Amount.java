package com.ha.model;

import java.math.BigDecimal;

public abstract class Amount {
	public abstract BigDecimal getTotal();
	public abstract boolean isLocalCurrencyAmount();
	public abstract boolean isCalculatedAmount();
}
