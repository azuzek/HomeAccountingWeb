package com.ha.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Entry extends UserFilteredObject {
	private Set<EntryLine> debits = new HashSet<>();
	private Set<EntryLine> credits = new HashSet<>();
	private String description;
	private LocalDate date;
	
	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Set<EntryLine> getDebits () { 
		return debits; 
	}
	
	public Set<EntryLine> getCredits () {
		return credits;
	}
	
	public int getNumberOfDebits () {
		return debits.size();
	}
	
	public int getNumberOfCredits () {
		return credits.size();
	}
	
	public void addCredit(EntryLine credit) {
		credits.add(credit);
	}
	
	public void addDebit (EntryLine debit) {
		debits.add(debit);
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getNonEmptyDescription() {
		if(this.getDescription() != null && !this.getDescription().equals("")) {
			return this.getDescription();
		} else {
			return
					this.getCredits()
						.stream()
						.findFirst()
						.get()
						.getAccount()
						.getName()+
					" a "+
					this.getDebits()
						.stream()
						.findFirst()
						.get()
						.getAccount()
						.getName();
		}
	}
}
