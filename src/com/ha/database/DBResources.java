package com.ha.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBResources {

	/**
	 * @param hadao
	 */
	public DBResources() {
	}
	private HADataSource haDataSource = null;
	private Connection connection = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	private Boolean autoCommit = true;
	
	public void setHaDataSource(HADataSource haDataSource) {
		this.haDataSource = haDataSource;
	}
	
	public HADataSource getHaDataSource() {
		return haDataSource;
	}
	
	private Connection getConnection() {
		if (connection == null) {
			try {
				connection = haDataSource.getDataSource().getConnection();
				connection.setAutoCommit(autoCommit);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return connection;
	}
	
	public boolean getAutoCommit() {
		return autoCommit;
	}
	
	public void setAutoCommit(Boolean autoCommit) {
		this.autoCommit = autoCommit;
		try {
			this.getConnection().setAutoCommit(autoCommit);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private Statement getStatement() {
		try {
			if (statement == null) {
				statement = this.getConnection().createStatement();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return statement;
	}
	public ResultSet executeStatement(String statement) {
		System.out.println("executeStatement - statement: "+statement);
		try {
			resultSet = this.getStatement().executeQuery(statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
	
	public void commit() {
		if (null != connection)
			try {
				connection.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	
	public void rollback() {
		if (null != connection) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void release() {
		try {
			if (null != resultSet) resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (null != statement) {
				statement.close();
				statement = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(!getAutoCommit()) closeConnection();
	}
	
	public void closeConnection() {
		try {
			if (null != connection) {
				connection.close();
				connection = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int executeUpdate(String statement) {
		int rowsAffected = 0;
		System.out.println("executeUpdate - statement: "+statement);
		try {
			rowsAffected = this.getStatement().executeUpdate(statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rowsAffected;
	}	
	public int executeInsert(String statement) {
		ResultSet resultSet;
		int rowsAffected = 0;
		int lastInsertId = 0;
		try {
			System.out.println("executeInsert - statement: "+statement);
			rowsAffected = this.getStatement().executeUpdate(statement);
			if (rowsAffected == 1) {
				resultSet = this.getStatement().executeQuery("SELECT LAST_INSERT_ID()");
				if(resultSet.next()) lastInsertId = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lastInsertId;
	}
}