package com.ha.listeners;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.ha.database.HADataSource;

/**
 * Application Lifecycle Listener implementation class ContextListener
 *
 */
@WebListener
public final class ContextListener implements ServletContextListener {
	
	private ServletContext context = null;

    /**
     * Default constructor. 
     */
    public ContextListener() {
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
    	System.out.println("Servlet Context Destroyed.");
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent event)  { 
    	context = event.getServletContext();
    	try {
    		HADataSource haDataSource = new HADataSource();
    		context.setAttribute("haDataSource", haDataSource);
    		System.out.println("Server Context Initialized.");
    	} catch (Exception e) {
    		System.out.println("Could not create HA DataSource bean.");
    	}
    }
}
