package com.ha.servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.ha.database.AccountDAO;
import com.ha.database.DBResources;
import com.ha.database.EntryDAO;
import com.ha.database.EntryLineDAO;
import com.ha.database.HADataSource;
import com.ha.database.QuoteDAO;
import com.ha.model.Account;
import com.ha.model.Amount;
import com.ha.model.BuySellPriceInstrument;
import com.ha.model.CalculatedAmount;
import com.ha.model.CreditEntryLine;
import com.ha.model.DebitEntryLine;
import com.ha.model.Entry;
import com.ha.model.EntryLine;
import com.ha.model.LocalCurrencyAmount;
import com.ha.model.Quote;
import com.ha.model.SinglePriceInstrument;
import com.ha.model.User;

/**
 * Servlet implementation class Entry
 */
@WebServlet(
	urlPatterns = {
		"/EntryController", 
		"/EntryController/addDebit", 
		"/EntryController/deleteDebit", 
		"/EntryController/addCredit",
		"/EntryController/deleteCredit"
	}
)
public class EntryController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HADataSource haDataSource;

    /**
     * Default constructor. 
     */
    public EntryController() {
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int debitAccounts = Integer.parseInt(request.getParameter("debitAccounts"));
		int creditAccounts = Integer.parseInt(request.getParameter("creditAccounts"));
		String[] debitAmountValues = new String[debitAccounts];
		String[] debitQuoteQuantityValues = new String[debitAccounts];
		String[] debitQuotePriceValues = new String[debitAccounts];
		String[] creditAmountValues = new String[creditAccounts];
		String[] creditQuoteQuantityValues = new String[creditAccounts];
		String[] creditQuotePriceValues = new String[creditAccounts];
		Amount amount;
		if("/EntryController/addDebit".equals(request.getServletPath())) {
			++debitAccounts;
		} else if ("/EntryController/deleteDebit".equals(request.getServletPath())) {
			--debitAccounts;
		} else if ("/EntryController/addCredit".equals(request.getServletPath())) {
			++creditAccounts;
		} else if ("/EntryController/deleteCredit".equals(request.getServletPath())) {
			--creditAccounts;
		} else {
			Session session = (Session) ((SessionFactory) this.getServletContext().getAttribute("sessionFactory")).openSession();
			session.beginTransaction();
			Boolean error = false;
			Set<Account> usedAccounts = new HashSet<>();
			EntryLine newEntryLine = null;
			User user = (User) request.getSession().getAttribute("user");
			@SuppressWarnings("unchecked")
			Set<Account> accountsByCredit = (Set<Account>) request.getSession().getAttribute("accountsByCredit");
			@SuppressWarnings("unchecked")
			Set<Account> accountsByDebit = (Set<Account>) request.getSession().getAttribute("accountsByDebit");
			String description = request.getParameter("description");
			Entry entry = new Entry();
			entry.setUser(user);
			entry.setDescription(description);
			String dateString = request.getParameter("date");
			try {
				LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				entry.setDate(date);
				// Create debit entry lines
				BigDecimal totalDebitAmount = new BigDecimal(0);
				for(int i=1; i<=debitAccounts & !error; i++) {
					int debitAccountId = Integer.parseInt(request.getParameter("debitAccount"+i));
					Account debitAccount = accountsByCredit.stream()
							.filter(p -> debitAccountId==p.getId())
							.findFirst()
							.get();
					if (usedAccounts.contains(debitAccount)) {
						error = true;
						request.setAttribute("errorMessage", "Una cuenta es utilizada más de una vez en este asiento.");
					} else {
						usedAccounts.add(debitAccount);
					}
					amount = readAmount(debitAccount, "debit", i, request, date, error, debitAmountValues, debitQuoteQuantityValues, debitQuotePriceValues);
					if(!error) {
						totalDebitAmount = totalDebitAmount.add(amount.getTotal());
						newEntryLine = new CreditEntryLine();
						newEntryLine.setAmount(amount);
						newEntryLine.setAccount(debitAccount);
						newEntryLine.setAccount(debitAccount);
					}
					entry.addDebit(newEntryLine);
				}
				// Create credit entry lines
				BigDecimal totalCreditAmount = new BigDecimal(0);
				for(int i=1; i<=creditAccounts & !error; i++) {
					int creditAccountId = Integer.parseInt(request.getParameter("creditAccount"+i));
					Account creditAccount = accountsByDebit.stream()
							.filter(p -> creditAccountId==p.getId())
							.findFirst()
							.get();
					if (usedAccounts.contains(creditAccount)) {
						error = true;
						request.setAttribute("errorMessage", "Una cuenta es utilizada más de una vez en este asiento.");
					} else {
						usedAccounts.add(creditAccount);
					}
					if(creditAccounts>1 || !creditAccount.isLocalCurrency()) {
						amount = readAmount(creditAccount, "credit", i, request, date, error, creditAmountValues, creditQuoteQuantityValues, creditQuotePriceValues);
					} else {
						amount = new LocalCurrencyAmount();
						((LocalCurrencyAmount) amount).setTotal(totalDebitAmount);
					}
					if(!error) {
						totalCreditAmount = totalCreditAmount.add(amount.getTotal());
						creditAccount.credit(amount.getTotal());
						newEntryLine = new DebitEntryLine();
						newEntryLine.setAccount(creditAccount);
						newEntryLine.setAccount(creditAccount);
						newEntryLine.setAmount(amount);
					}
					entry.addCredit(newEntryLine);
				}
				if(0!=totalDebitAmount.compareTo(totalCreditAmount)) {
					request.setAttribute("errorMessage", "El importe total de los débitos y los créditos no coincide.");
					error=true;
				}
			} catch (IllegalArgumentException e) {
				request.setAttribute("errorMessage", "Formato de fecha incorrecto.");
				error = true;
			}
			if(!error) {
				debitAccounts=1;
				creditAccounts=1;
				session.save(entry);
				session.getTransaction().commit();
			} else {
				session.getTransaction().rollback();
			}
			request.setAttribute("description", description);
			request.setAttribute("date", dateString);
			// TODO: Commented out due to migration to Hibernate. Remove once finished with migration.
			// persistEntry(entry, user);
			session.close();
		}
		request.setAttribute("debitAmountValues", debitAmountValues);
		request.setAttribute("creditAmountValues", creditAmountValues);
		request.setAttribute("action", "entry");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/Main.jsp?debitAccounts="+debitAccounts+"&creditAccounts="+creditAccounts);
		if (dispatcher != null) {
			dispatcher.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private BigDecimal readBigDecimalInput(HttpServletRequest request, String field, String fieldName, Account account, String[] values, int index, Boolean error) {
		BigDecimal value = null;
		try {
			String parameterValue = request.getParameter(field+index);
			if(!parameterValue.isEmpty()) {
				value = new BigDecimal(parameterValue);
			}
		} catch (NumberFormatException e) {
			request.setAttribute(values[0], "Formato de "+fieldName+" incorrecto para la cuenta "+account.getName());
			error = true;
		}
		return value;
	}
	
	private Amount readAmount(Account account, String debitOrCredit, int index, HttpServletRequest request, LocalDate date, Boolean error, String[] amountValues, String[] quoteQuantityValues, String[] quotePriceValues) {
		Amount amount = null;
		BigDecimal debitAmount = readBigDecimalInput(request, debitOrCredit+"Amount", "importe", account, amountValues, index, error);
		if (account.isLocalCurrency()) {
			if(debitAmount != null) {
				account.debit(debitAmount);
				amount = new LocalCurrencyAmount();
				((LocalCurrencyAmount) amount).setTotal(debitAmount);
			} else {
				request.setAttribute("errorMessage", "El importe no puede estar vacío.");
				error = true;
			}
		} else { // Account has calculated amounts
			BigDecimal debitQuoteQuantity =  readBigDecimalInput(request, debitOrCredit+"QuoteQuantity", "cantidad", account, quoteQuantityValues, index, error);
			BigDecimal debitQuoteUnitPrice = readBigDecimalInput(request, debitOrCredit+"QuoteUnitPrice", "precio unitario", account, quotePriceValues, index, error);
			if(debitAmount != null) {
				if(debitQuoteQuantity != null) {
					if (debitQuoteUnitPrice != null) {
						if(debitAmount.compareTo(debitQuoteQuantity.multiply(debitQuoteUnitPrice)) != 0) {
							request.setAttribute("errorMessage", "El precio unitario por la cantidad no es igual al importe total para la cuenta "+account.getName());
							error = true;
						}
					} else {
						debitQuoteUnitPrice = debitAmount.divide(debitQuoteQuantity);
					}
				} else {
					if(debitQuoteUnitPrice != null) {
						debitQuoteQuantity = debitAmount.divide(debitQuoteUnitPrice);
					} else {
						request.setAttribute("errorMessage", "Falta ingresar la cantidad o el precio unitario para la cuenta "+account.getName());
						error = true;
					}
				}
			} else {
				// The total amount is empty. Both quantity and unit price are expected to be defined here.
				if(debitQuoteQuantity == null || debitQuoteUnitPrice == null) {
					request.setAttribute("errorMessage", "Flata ingresar la cantidad y/o el precio unitario para la cuenta "+account.getName());
					error = true;
				}
			}
			Quote quote = new Quote();
			quote.setInstrument(account.getInstrument());
			quote.setDate(date);
			quote.setPrice(debitQuoteUnitPrice);
			amount = new CalculatedAmount();
			((CalculatedAmount) amount).setQuantity(debitQuoteQuantity);
			((CalculatedAmount) amount).setQuote(quote);
			if(account.getInstrument().isSinglePrice()) {
				((SinglePriceInstrument) account.getInstrument()).addQuote(quote);
			} else { // The instrument has buy and sell price. This is a debit line, so we are buying the instrument.
				((BuySellPriceInstrument) account.getInstrument()).addBuyQuote(quote);
			}
		}
		return amount;
	}
	
	private void persistEntry(Entry entry, User user) {
		DBResources dbResources = new DBResources();
		dbResources.setHaDataSource(haDataSource);
		dbResources.setAutoCommit(false);
		AccountDAO accountDAO = new AccountDAO();
		accountDAO.setDbResources(dbResources);
		accountDAO.setUser(user);
		EntryDAO entryDAO = new EntryDAO();
		entryDAO.setDbResources(dbResources);
		entryDAO.setUser(user);
		EntryLineDAO entryLineDAO = new EntryLineDAO();
		entryLineDAO.setDbResources(dbResources);
		entryLineDAO.setUser(user);
		QuoteDAO quoteDAO = new QuoteDAO();
		quoteDAO.setDbResources(dbResources);
		int newEntryId = entryDAO.insertOrUpdateModelObject(entry);
		List<Set<EntryLine>> entryLinesList = new ArrayList<Set<EntryLine>>();
		entryLinesList.add(entry.getDebits());
		entryLinesList.add(entry.getCredits());
		for(Set<EntryLine> entryLines : entryLinesList) {
			for(EntryLine entryLine : entryLines) {
				// TODO: Make sure that EntryLine has a reference to the Entry so that EntryLineDAO does not need an entry ID
				// entryLine.setEntryId(newEntryId);
				if(entryLine.getAmount().isCalculatedAmount()) {
					quoteDAO.insertOrUpdateModelObject(((CalculatedAmount) entryLine.getAmount()).getQuote());
				}
				entryLineDAO.insertOrUpdateModelObject(entryLine);
				accountDAO.updateModelObject(entryLine.getAccount());
				// TODO: Persist Quote and possibly Instrument too?
				// TODO: QuoteDAO does not handle the two Quote types. They could be buy or sell if they belong to a BuySellPriceInstrument
			}
		}
		dbResources.commit();
		dbResources.closeConnection();
		dbResources.release();
	}
}
