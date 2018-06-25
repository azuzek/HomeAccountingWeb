package com.ha.model;

import java.math.BigDecimal;

import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class CalculatedAmount extends Amount {
	
	@ManyToOne
	@JoinColumn(name = "quote_id", foreignKey = @ForeignKey(name = "entry_line_quote_fk"))		
	private Quote quote;
	
	private BigDecimal quantity;
	
	public Quote getQuote() {
		return quote;
	}
	
	public void setQuote(Quote quote) {
		this.quote = quote;
	}
	
	public BigDecimal getQuantity() {
		return quantity;
	}
	
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	@Override
	public BigDecimal getTotal() {
		return getQuote().getPrice().multiply(getQuantity());
	}

	@Override
	public boolean isLocalCurrencyAmount() {
		return false;
	}

	@Override
	public boolean isCalculatedAmount() {
		return true;
	}
	
	
}
