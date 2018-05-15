package com.ha.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ha.database.CategoryDAO;
import com.ha.database.DBResources;
import com.ha.database.HADataSource;
import com.ha.model.Category;
import com.ha.model.User;

/**
 * Servlet implementation class CategoryController
 */
@WebServlet( "/CategoryController" )
public class CategoryController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HADataSource haDataSource;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CategoryController() {
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
		// This only handles the "add" case because data input is needed. 
		// Deletions are handled by CategoriesController directly.
		// TODO: Handle edits (change of category name)
		DBResources dbResources = new DBResources();
		dbResources.setHaDataSource(haDataSource);
		Category category;
		CategoryDAO categoryDAO = new CategoryDAO();
		User user = (User) request.getSession().getAttribute("user");
		categoryDAO.setUser(user);
		categoryDAO.setDbResources(dbResources);
		category = new Category();
		if (!request.getParameter("id").isEmpty()) {
			category.setId(Integer.parseInt(request.getParameter("id")));
		}
		category.setParentId(Integer.parseInt(request.getParameter("parentId")));
		category.setName(request.getParameter("name"));
		category.setUserId(user.getId());
		Category parent = user.findCategory(category.getParentId());
		parent.addChild(category);
		category.setType(parent.getType());
		categoryDAO.insertModelObject(category);
		request.setAttribute("action", "accounts");
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
