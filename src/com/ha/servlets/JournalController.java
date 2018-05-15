package com.ha.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ha.database.DBResources;
import com.ha.database.EntryDAO;
import com.ha.database.HADataSource;
import com.ha.model.Entry;
import com.ha.model.PagedList;
import com.ha.model.User;

/**
 * Servlet implementation class JournalController
 */
@WebServlet("/JournalController")
public class JournalController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HADataSource haDataSource;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public JournalController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
    	// get HADataSource from Servlet context.
    	haDataSource = (HADataSource) this.getServletContext().getAttribute("haDataSource");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO: This is initialization is being handled in LoginController and entries is being stored in the session. Remove this section.
		User user = (User) request.getSession().getAttribute("user");
		DBResources dbResources = new DBResources();
		dbResources.setHaDataSource(haDataSource);
		dbResources.setAutoCommit(false);
		EntryDAO entryDAO = new EntryDAO();
		entryDAO.setDbResources(dbResources);
		entryDAO.setUser(user);
		PagedList<Entry> entries = new PagedList<>(entryDAO.selectModelObjects(), 20);
		request.setAttribute("entries", entries);
		request.setAttribute("action", "journal");
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
