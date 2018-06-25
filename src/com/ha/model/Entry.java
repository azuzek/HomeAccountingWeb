package com.ha.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name = "Entry")
@Table(name = "ENTRY")
public class Entry extends ModelObject {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "entry_user_fk"))
	private User user;
	
	@OneToMany(mappedBy = "entry", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<EntryLine> debits = new HashSet<>();
	
	@OneToMany(mappedBy = "entry", cascade = CascadeType.ALL, orphanRemoval = true)
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
