package com.ha.servlets;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.ha.model.Account;
import com.ha.model.Category;
import com.ha.model.User;

/**
 * Servlet implementation class AccountController
 */
@WebServlet(urlPatterns = { "/AccountsController", "/AccountsController/add", "/AccountsController/edit", "/AccountsController/deactivate" })
public class AccountsController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HADataSource haDataSource;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AccountsController() {
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
		Account account = null;
		int parentCategoryId = 0;
		User user = (User) request.getSession().getAttribute("user");
		DBResources dbResources = new DBResources();
		dbResources.setHaDataSource(haDataSource);
		CategoryDAO categoryDAO = new CategoryDAO();
		categoryDAO.setUser(user);
		categoryDAO.setDbResources(dbResources);
		AccountDAO accountDAO = new AccountDAO();
		accountDAO.setUser(user);
		accountDAO.setDbResources(dbResources);
		switch(request.getServletPath()) {
		case "/AccountsController/add":
			parentCategoryId = Integer.parseInt(request.getParameter("parentId"));
			request.setAttribute("operation", "add");
			request.setAttribute("action", "account");
			break;
		case "/AccountsController/edit":
			account = accountDAO.findModelObject(Integer.parseInt(request.getParameter("accountId")));
			parentCategoryId = account.getCategoryId();
			request.setAttribute("accountId", account.getId());
			request.setAttribute("name", account.getName());
			request.setAttribute("balance", account.getBalance());
			request.setAttribute("operation", "edit");
			request.setAttribute("action", "account");
			break;
		case "/AccountsController/deactivate":
			int id = Integer.parseInt(request.getParameter("accountId"));
			System.out.println("AccountsController - Deactivate - accountId: "+id);
			request.setAttribute("accountId", id);
			request.setAttribute("message", "Quiere desactivar la cuenta?");
			request.setAttribute("action", "deactivateAccount");
			RequestDispatcher dispatcher = request.getRequestDispatcher("/Main.jsp");
			if (dispatcher != null) {
				dispatcher.forward(request, response);
			}
			break;
		default: break;
		}
		// TODO: Refactor this code. filtered categories is only needed in add and edit cases.
		Category parentCategory = categoryDAO.findModelObject(parentCategoryId);
		request.setAttribute("parentCategory", parentCategory);
		Set<Category> filteredCategories = categoryDAO.selectModelObjects()
				.stream()
				.filter(e -> parentCategory.getType() == e.getType())
				.collect(Collectors.toSet());
		request.setAttribute("categories", filteredCategories);
		dbResources.release();
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
