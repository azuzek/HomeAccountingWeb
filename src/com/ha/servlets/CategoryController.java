package com.ha.servlets;

import java.io.IOException;

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
		Session session = (Session) ((SessionFactory) this.getServletContext().getAttribute("sessionFactory")).openSession();
		/* TODO: Commented out due to migration to Hibernate. Remove when migration complete.
		 * DBResources dbResources = new DBResources();
		 * dbResources.setHaDataSource(haDataSource);
		 * CategoryDAO categoryDAO = new CategoryDAO();
		 */
		Category category;
		User user = (User) request.getSession().getAttribute("user");
		/* TODO: Commented out due to migration to Hibernate. Remove when migration complete.
		categoryDAO.setUser(user);
		categoryDAO.setDbResources(dbResources);
		*/
		Query<?> query = session.createQuery(
				"select c " +
				"from Category c " +
				"where c.id = :categoryId");
		if (!request.getParameter("id").isEmpty()) {
			query.setParameter("categoryId", request.getParameter("id"));
			category = (Category) query.uniqueResult();
			Hibernate.initialize(category);
			// category.setId(Integer.parseInt(request.getParameter("id")));
		} else {
			category = new Category();
		}
		query.setParameter("categoryId", request.getParameter("parentId"));
		Category parent = (Category) query.uniqueResult();
		Hibernate.initialize(category);
		category.setParent(parent);
		category.setName(request.getParameter("name"));
		category.setUser(user);
		// Category parent = user.findCategory(category.getParentId());
		// parent.addChild(category);
		category.setType(parent.getType());
		//categoryDAO.insertModelObject(category);
		request.setAttribute("action", "accounts");
		// dbResources.release();
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
