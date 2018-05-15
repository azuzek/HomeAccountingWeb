package com.ha.model;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Category extends UserFilteredObject {
	private int parentId;
	private AccountType type;
	private boolean fixed;
	private String name;
	private Set<Category> children = new HashSet<>();
	private Set<Account> accounts = new HashSet<>();
		
	public static Category findCategory(Set<Category> categories, int id) {
		Category result = null;
		for (Category category : categories ) {
			if (category.getId() == id) {
				result = category;
				break;
			}
			else {
				result = findCategory(category.getChildren(),id);
				if (result != null) break;
			}
		}
		return result;
	}
	
	public int getParentId() {
		return parentId;
	}
	
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	
	public AccountType getType() {
		return type;
	}

	public void setType(AccountType type) {
		this.type = type;
	}
	
	public boolean isFixed() {
		return fixed;
	}
	
	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Set<Category> getChildren() {
		return children;
	}
	
	public void addChild(Category category) {
		children.add(category);
	}
	public void removeChild(Category category) {
		children.remove(category);
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
	
	public void addAccount(Account account) {
		accounts.add(account);
	}
	
	public void removeAccount(Account account) {
		accounts.remove(account);
	}
}
