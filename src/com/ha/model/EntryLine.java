package com.ha.model;

public abstract class EntryLine extends UserFilteredObject {
	private Account account;
	private int entryId;
	private int accountId;
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

	/**
	 * @return the accountId
	 */
	public int getAccountId() {
		return accountId;
	}

	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return the entryId
	 */
	public int getEntryId() {
		return entryId;
	}

	/**
	 * @param entryId the entryId to set
	 */
	public void setEntryId(int entryId) {
		this.entryId = entryId;
	}
}
