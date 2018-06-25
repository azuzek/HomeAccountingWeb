/**
 * 
 */
package com.ha.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * @author MLA
 *
 */
@Entity(name = "Account")
@Table(name = "ACCOUNT")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="TYPE", discriminatorType=DiscriminatorType.CHAR)
@DiscriminatorValue(value = "Z")
public abstract class Account extends ModelObject {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "account_user_fk"))
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "account_category_fk"))
	private Category category;
	
	private BigDecimal balance;
	
	private boolean active = true;
	
	@Column(name="local_currency")
	private boolean localCurrency = true;
	
	@ManyToOne
    @JoinColumn(name = "instrument_id", foreignKey = @ForeignKey(name = "account_instrument_fk"))
	private Instrument instrument;
	
	@Transient
	private int debitFrequency = 0;
	
	@Transient
	private int creditFrequency = 0;
	
	public Instrument getInstrument() {
		return instrument;
	}
	public void setInstrument(Instrument instrument) {
		this.instrument = instrument;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public static Account getAccount(char type) {
		Account account = null;
		switch(type) {
		case 'A': account = new AssetAccount();
			  break;
		case 'E': account = new ExpenseAccount();
			  break;
		case 'L': account = new LiabilityAccount();
	  		  break;
		case 'R': account = new RevenueAccount();
	  		  break;
		default: account = null;
		}
		return account;
	}
	abstract public void debit(BigDecimal amount);
	abstract public void credit(BigDecimal amount);
	abstract public char getType();
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 *  @return the category id
	 */
	public Category getCategory() {
		return category;
	}
	
	/**
	 * @param categoryId the category id to set
	 */
	public void setCategory(Category aCategory) {
		this.category = aCategory;
	}
	
	/**
	 * @return the balance
	 */
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public boolean isLocalCurrency() {
		return localCurrency;
	}
	public void setLocalCurrency(Boolean localCurrency) {
		this.localCurrency = localCurrency;
	}
	public int getCreditFrequency() {
		return creditFrequency;
	}
	public void setCreditFrequency(int frequency) {
		creditFrequency = frequency;
	}
	public int getDebitFrequency() {
		return debitFrequency;
	}
	public void setDebitFrequenty(int frequencty) {
		debitFrequency = frequencty;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
