package com.ha.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity (name = "CreditEntryLine")
@DiscriminatorValue(value = "C")
public class CreditEntryLine extends EntryLine {
	public char getAction() {return 'C';}
}
