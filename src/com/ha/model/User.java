/**
 * 
 */
package com.ha.model;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author MLA
 *
 */
@Entity(name = "User")
@Table(name = "USER")
public class User extends ModelObject {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String userId;
	private String password;
	private Set<Account> accounts = new HashSet<>();
	private Set<Category> categories = new HashSet<>();
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Set<Account> getAccounts() {
		return accounts;
	}
	
	public Set<Account> getActiveAccounts() {
		return this.getAccounts()
				.stream()
				.filter(e -> e.isActive())
				.collect(Collectors.toSet());
	}
	
	public Set<Account> getInactiveAccounts() {
		return this.getAccounts()
				.stream()
				.filter(e -> !e.isActive())
				.collect(Collectors.toSet());
	}
	
	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}
	public Set<Account> getAccountsByDebitFrequency() {
		// TODO This comparator class should be provided by Account
		class AccountDebitFrequencyComparator implements Comparator<Account>{

		    @Override
		    public int compare(Account account1, Account account2) {
		    	if(account1.getDebitFrequency()==account2.getDebitFrequency()) {
		    		return 
		    			Integer.valueOf(account2.getId()).
		    				compareTo(Integer.valueOf(account1.getId()));
		    	} else {
			        return 
			        	Integer.valueOf(account2.getDebitFrequency()).
			        		compareTo(Integer.valueOf(account1.getDebitFrequency()));
		    	}
		    }

		}
		SortedSet<Account> accounts = new TreeSet<>(new AccountDebitFrequencyComparator());
		accounts.addAll(this.getActiveAccounts());
		return accounts;
	}

	public Set<Account> getAccountsByCreditFrequency() {
		// TODO This comparator class should be provided by Account
		class AccountCreditFrequencyComparator implements Comparator<Account>{

		    @Override
		    public int compare(Account account1, Account account2) {
		    	if(account1.getCreditFrequency()==account2.getCreditFrequency()) {
		    		return 
		    			Integer.valueOf(account2.getId()).
		    				compareTo(Integer.valueOf(account1.getId()));
		    	} else {
			        return 
			        	Integer.valueOf(account2.getCreditFrequency()).
			        		compareTo(Integer.valueOf(account1.getCreditFrequency()));
		    	}
		    }

		}
		SortedSet<Account> accounts = new TreeSet<>(new AccountCreditFrequencyComparator());
		accounts.addAll(this.getActiveAccounts());
		return accounts;
	}
	public Set<Category> getCategories() {
		return categories;
	}
	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}
	public Category findCategory(int id) {
		return Category.findCategory(getCategories(), id);
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
