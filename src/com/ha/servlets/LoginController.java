package com.ha.servlets;

import java.io.IOException;
import java.util.Arrays;

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

import javax.servlet.http.Cookie;

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
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginController() {
        super();
    }
    
    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Session session = (Session) ((SessionFactory) this.getServletContext().getAttribute("sessionFactory")).openSession();
		if (request.getParameter("ok") != null) {
			// User pressed OK button. Process login
			Query<?> query = session.createQuery(
					"select u " +
					"from User u " +
					"where u.userId = :userId")
				.setParameter("userId", request.getParameter("userId"));
			User user = (User) query.uniqueResult();
			Hibernate.initialize(user);
			if (user != null) {
				if (request.getParameter("password").equals(user.getPassword())) {
					// Call Entry JSP
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
					PagedList<Entry> entries = new PagedList<>(user.getEntries(), 20);
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
		}
		else if(request.getParameter("cancel") != null) {
			// If user pressed Cancel button, go back to login page.
			// TODO: See if userid and password attributes need to be reset at this point.
			request.setAttribute("action", "login");
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("/Main.jsp");
		session.close();
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
