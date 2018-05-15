package com.ha.servlets;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;

import com.ha.database.DBResources;
import com.ha.database.EntryDAO;
import com.ha.database.HADataSource;
import com.ha.database.UserDAO;
import com.ha.model.Account;
import com.ha.model.Entry;
import com.ha.model.PagedList;
import com.ha.model.User;

/**
 * Servlet implementation class LogintController
 */
@WebServlet(description = "Handle logins. Open Entry page if successfull, return back to login if not.", urlPatterns = { "/LoginController" })
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HADataSource haDataSource;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginController() {
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
		if (request.getParameter("ok") != null) {
			// User pressed OK button. Process login
			DBResources dbResources = new DBResources();
			dbResources.setHaDataSource(haDataSource);
			UserDAO userDAO = new UserDAO();
			userDAO.setDbResources(dbResources);
			User user = userDAO.findUser(request.getParameter("userId"));
			if (user != null) {
				if (request.getParameter("password").equals(user.getPassword())) {
					// Call Entry JSP
					userDAO.populateCategories(user);
					userDAO.populateAccounts(user);
					String calculatedAmountAccounts = Arrays.toString(
							user.getAccounts().stream()
							.filter(p -> !p.isLocalCurrency())
							.map(Account::getId)
							.map(p -> "'"+p+"'")
							.toArray());
					if(request.getParameter("rememberMe") != null) {
						Cookie rememberMeCookie = new Cookie("rememberMe",user.getUserId()+"~"+user.getPassword());
						rememberMeCookie.setMaxAge(60*60*24*180); // Store cookie for 6 months
						response.addCookie(rememberMeCookie);
					}
					EntryDAO entryDAO = new EntryDAO();
					DBResources dbResources2 = new DBResources();
					// TODO: Go back to dbResources if dbResources2 is not needed. It is probably not needed.
					dbResources2.setHaDataSource(haDataSource);
					entryDAO.setDbResources(dbResources2);
					entryDAO.setUser(user);
					dbResources2.setAutoCommit(false);
					PagedList<Entry> entries = new PagedList<>(entryDAO.selectModelObjects(), 20);
					dbResources2.commit();
					dbResources2.release();
					request.getSession().setAttribute("entries", entries);
					request.getSession().setAttribute("user", user);
					request.getSession().setAttribute("calculatedAmountAccounts", calculatedAmountAccounts);
					request.setAttribute("action", "entry");
				} else {
					// Call login page with error message.
					request.setAttribute("errorMessage", "Password is invalid.");
					request.setAttribute("action", "login");
				}
			} else {
				// Call login page with error message
				request.setAttribute("errorMessage", "User id not found.");
				request.setAttribute("action", "login");
			}
			dbResources.release();
		}
		else if(request.getParameter("cancel") != null) {
			// If user pressed Cancel button, go back to login page.
			// TODO: See if userid and password attributes need to be reset at this point.
			request.setAttribute("action", "login");
		}
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
