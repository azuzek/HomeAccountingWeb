package com.ha.servlets;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.ha.database.AccountDAO;
import com.ha.database.CategoryDAO;
import com.ha.database.DBResources;
import com.ha.database.HADataSource;
import com.ha.database.InstrumentDAO;
import com.ha.database.UserDAO;
import com.ha.model.Account;
import com.ha.model.Category;
import com.ha.model.Instrument;
import com.ha.model.User;

/**
 * Servlet implementation class AccountController
 */
@WebServlet("/AccountController")
public class AccountController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HADataSource haDataSource;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AccountController() {
        super();
    }  
    
    /**
     * get a connection to the database
     */
    public void init() throws ServletException {
    	// get HADataSource from Servlet context.
    	haDataSource = (HADataSource) this.getServletContext().getAttribute("haDataSource");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO: Error handling. Name could be empty or too long. Balance could not be a valid BigDecimal string.
		Session session = (Session) ((SessionFactory) this.getServletContext().getAttribute("sessionFactory")).openSession();
		Boolean error = false;
		User user = (User) request.getSession().getAttribute("user");
		DBResources dbResources = new DBResources();
		dbResources.setHaDataSource(haDataSource);
		CategoryDAO categoryDAO = new CategoryDAO();
		categoryDAO.setUser(user);
		categoryDAO.setDbResources(dbResources);
		if(request.getParameter("ok") != null) {
			Account account = null;
			Category category = null;
			UserDAO userDAO = new UserDAO();
			userDAO.setDbResources(dbResources);
			AccountDAO accountDAO = new AccountDAO();
			accountDAO.setUser(user);
			accountDAO.setDbResources(dbResources);
			Query<?> query = session.createQuery(
					"select c " +
					"from Category c " +
					"where u.id = :categoryId");
			if("add".equals(request.getParameter("operation"))) {
				try {
					account = Account.getAccount(request.getParameter("type").charAt(0));
					account.setName(request.getParameter("name"));
					query.setParameter("categoryId", request.getParameter("parentId"));
					category = (Category) query.uniqueResult();
					Hibernate.initialize(category);
					account.setCategory(category);
					account.setBalance(new BigDecimal(request.getParameter("balance")));
					if(request.getParameter("isLocalCurrency").equals("No")) {
						account.setLocalCurrency(false);
						InstrumentDAO instrumentDAO = new InstrumentDAO();
						instrumentDAO.setDbResources(dbResources);
						instrumentDAO.setUser(user);
						Instrument instrument = null;
						if(request.getParameter("instrumentId").equals("new")) {
							instrument = Instrument.getInstrument(request.getParameter("isSinglePrice").equals("Yes")? 'S': 'D');
							instrument.setName(request.getParameter("instrumentName"));
							instrument.setCode(request.getParameter("instrumentCode"));
							instrumentDAO.insertModelObject(instrument);
						} else {
							instrument = instrumentDAO.findModelObject(Integer.parseInt(request.getParameter("instrumentId")));	
						}
						account.setInstrument(instrument);
					}
					accountDAO.insertModelObject(account);
				} catch (NumberFormatException e) {
					error = true;
					request.setAttribute("errorMessage", "El saldo no es un importe válido.");
				} finally {
					request.setAttribute("name", request.getParameter("name"));
					request.setAttribute("balance", request.getParameter("balance"));
				}
			} else if ("edit".equals(request.getParameter("operation"))) {
				account = accountDAO.findModelObject(Integer.valueOf(request.getParameter("accountId")));
				account.setName(request.getParameter("name"));
				query.setParameter("categoryId", request.getParameter("category"));
				category = (Category) query.uniqueResult();
				Hibernate.initialize(category);
				account.setCategory(category);
				accountDAO.updateModelObject(account);
			}
			if (account.getName() == null || account.getName().equals("")) {
				error = true;
				request.setAttribute("errorMessage", "El nombre de la cuenta no puede quedar vacío.");
			}
			userDAO.populateCategories(user);
			userDAO.populateAccounts(user);
		}
		// Cancel case does not need to be handled. Just allow the code to call Main again with accounts as action.
		request.setAttribute("categories", categoryDAO.selectModelObjects());
		if(!error) {
			request.setAttribute("action", "accounts");
		} else {
			request.setAttribute("action", "account");
		}
		dbResources.release();
		session.close();
		RequestDispatcher dispatcher = request.getRequestDispatcher("/Main.jsp");
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

}
