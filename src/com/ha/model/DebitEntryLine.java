package com.ha.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "DebitEntryLine")
@DiscriminatorValue(value = "D")
public class DebitEntryLine extends EntryLine {
	public char getAction() {return 'D';}
}
