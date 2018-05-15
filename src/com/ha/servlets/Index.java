package com.ha.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Index
 */
@WebServlet("/Index")
public class Index extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Index() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = null;
		String parameters = null;
		Cookie[] cookies = request.getCookies();
		if( cookies != null ){
			for (Cookie cookie : cookies){
				if(cookie.getName().equals("rememberMe")) {
					String[]userIdAndPassword = cookie.getValue().split("~");
					parameters = "?ok=OK&userId="+userIdAndPassword[0]+"&password="+userIdAndPassword[1];
					dispatcher = request.getRequestDispatcher("/LoginController"+parameters);
				}
			}
			if(parameters == null) {
				request.setAttribute("action", "login");
				dispatcher = request.getRequestDispatcher("Main.jsp");
			}
		} else {
			request.setAttribute("action", "login");
			dispatcher = request.getRequestDispatcher("Main.jsp");
		}
		if(dispatcher != null) {
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
