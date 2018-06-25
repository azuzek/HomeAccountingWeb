package com.ha.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ha.database.AccountDAO;
import com.ha.database.CategoryDAO;
import com.ha.database.DBResources;
import com.ha.database.HADataSource;
import com.ha.database.UserDAO;
import com.ha.model.Account;
import com.ha.model.Category;
import com.ha.model.User;

/**
 * Servlet implementation class DeleteCategoryController
 */
@WebServlet("/DeleteCategoryController")
public class DeleteCategoryController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HADataSource haDataSource;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteCategoryController() {
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
			int id = Integer.parseInt(request.getParameter("id"));
			DBResources dbResources = new DBResources();
			dbResources.setHaDataSource(haDataSource);
			User user = (User) request.getSession().getAttribute("user");
			Category category = user.findCategory(id);
			AccountDAO accountDAO = new AccountDAO();
			accountDAO.setDbResources(dbResources);
			accountDAO.setUser(user);
			for(Account account : category.getInactiveAccounts()) {
				account.setCategory(category.getParent());
				accountDAO.updateModelObject(account);
			}
			UserDAO userDAO = new UserDAO();
			userDAO.setDbResources(dbResources);
			CategoryDAO categoryDAO = new CategoryDAO();
			categoryDAO.setDbResources(dbResources);
			categoryDAO.setUser(user);
			categoryDAO.deleteModelObject(category);
			userDAO.populateCategories(user);
			userDAO.populateAccounts(user);
			request.setAttribute("categories", categoryDAO.selectModelObjects());
			dbResources.release();
		}
		// User can press cancel. Code implicitly just calls back the accounts page.
		request.setAttribute("action", "accounts");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/Main.jsp");
		if (dispatcher != null) {
			dispatcher.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
