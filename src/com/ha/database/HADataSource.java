package com.ha.database;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class HADataSource {
	private DataSource dataSource;
	public HADataSource () {
    	try {
    		// get DataSource
    		Context initContext = new InitialContext();
    		Context envContext = (Context)initContext.lookup("java:/comp/env");
    		dataSource = (DataSource)envContext.lookup("jdbc/hadb");
    	} catch (NamingException e) {
    		e.printStackTrace();
    	}
	}

	public DataSource getDataSource() {
		return dataSource;
	}
}
