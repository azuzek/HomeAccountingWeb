/**
 * 
 */
package com.ha.model;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author MLA
 *
 */

@Entity(name = "LiabilityAccount")
@DiscriminatorValue(value = "L")
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
