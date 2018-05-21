package com.ha.listeners;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import com.ha.database.HADataSource;

/**
 * Application Lifecycle Listener implementation class ContextListener
 *
 */
@WebListener
public final class ContextListener implements ServletContextListener {
	
	private ServletContext context = null;
	private SessionFactory sessionFactory;

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
		// A SessionFactory is set up once for an application!
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.configure() // configures settings from hibernate.cfg.xml
				.build();
		try {
			sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
			context.setAttribute("sessionFactory", sessionFactory);
		}
		catch (Exception e) {
			// The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
			// so destroy it manually.
			StandardServiceRegistryBuilder.destroy( registry );
		}
/*    	try {
    		HADataSource haDataSource = new HADataSource();
    		context.setAttribute("haDataSource", haDataSource);
    		System.out.println("Server Context Initialized.");
    	} catch (Exception e) {
    		System.out.println("Could not create HA DataSource bean.");
    	} */
    }

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
