/**
 * 
 */
package com.ha.model;

import java.math.BigDecimal;

/**
 * @author MLA
 *
 */
public class LiabilityAccount extends Account {
public char getType() {
	return 'L';
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
