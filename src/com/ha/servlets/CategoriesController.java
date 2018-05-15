package com.ha.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ha.database.DBResources;
import com.ha.database.HADataSource;
import com.ha.model.Category;
import com.ha.model.User;

/**
 * Servlet implementation class CategoriesController
 */
@WebServlet(
	urlPatterns = { 
		"/CategoriesController/add", 
		"/CategoriesController/delete"
	}
)
public class CategoriesController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HADataSource haDataSource;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CategoriesController() {
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
		switch(request.getServletPath()) {
		case "/CategoriesController/add":
			request.setAttribute("parentId", Integer.parseInt(request.getParameter("parentId")));
			request.setAttribute("action", "category");
			break;
		case "/CategoriesController/delete":
			int id = Integer.parseInt(request.getParameter("id"));
			DBResources dbResources = new DBResources();
			dbResources.setHaDataSource(haDataSource);
			User user = (User) request.getSession().getAttribute("user");
			Category category = user.findCategory(id);
			if(category.getInactiveAccounts().isEmpty()) {
				request.setAttribute("message", "Desea eliminar la categoria?");
			} else {
				request.setAttribute("message", "La categoria es padre de cuentas inactivas. Si la categoria se elimina, sus cuentas inactivas pasaran al padre. Desea continuar?");
			}
			request.setAttribute("id", id);
			request.setAttribute("action", "deleteCategory");
			break;
		default: break;
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
