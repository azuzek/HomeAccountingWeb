package com.ha.model;

import java.math.BigDecimal;

import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;

@Embeddable
@MappedSuperclass
public abstract class Amount {
	public abstract BigDecimal getTotal();
	public abstract boolean isLocalCurrencyAmount();
	public abstract boolean isCalculatedAmount();
}
