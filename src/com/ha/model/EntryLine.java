package com.ha.model;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "EntryLine")
@Table(name = "ENTRY_LINE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="ACTION", discriminatorType=DiscriminatorType.CHAR)
@DiscriminatorValue(value = "Z")
public abstract class EntryLine extends ModelObject {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "account_id", foreignKey = @ForeignKey(name = "entry_line_account_fk"))	
	private Account account;
	
	@ManyToOne
	@JoinColumn(name = "entry_id", foreignKey = @ForeignKey(name = "entry_line_entry_fk"))
	private Entry entry;
	
	private Amount amount;
	
	public static EntryLine getEntryLine(char action) {
		EntryLine entryLine = null;
		switch(action) {
		case 'D': entryLine = new DebitEntryLine();
			break;
		case 'C': entryLine = new CreditEntryLine();
			break;
		default: entryLine = null;
		}
		return entryLine;
	}
	
	public abstract char getAction();
	
	public void setAccount(Account a) {
		account = a;
	}

	public Account getAccount() {
		return account;
	}
	
	public void setAmount(Amount a) {
		amount = a;
	}
	
	public Amount getAmount() {
		return amount;
	}

	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}
}
