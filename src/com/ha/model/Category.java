package com.ha.model;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name = "Category")
@Table(name = "CATEGORY")
public class Category extends ModelObject {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	private Category parent;
	@ManyToOne
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "category_user_fk"))
	private User user;
	@Convert(converter = AccountTypeConverter.class)
	private AccountType type;
	private boolean fixed;
	private String name;
	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Category> children = new HashSet<>();
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
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
	
	public Category getParent() {
		return parent;
	}
	
	public void setParent(Category aParent) {
		this.parent = aParent;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
