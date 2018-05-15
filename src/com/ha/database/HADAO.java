/**
 * 
 */
package com.ha.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.ha.model.ModelObject;
import com.ha.model.User;

/**
 * @author MLA
 *
 */
public abstract class HADAO <T extends ModelObject> {
	DBResources dbResources;
	
	public DBResources getDbResources() {
		return dbResources;
	}

	public void setDbResources(DBResources dbResources) {
		this.dbResources = dbResources;
	}
	
	public int insertModelObject(T modelObject) {
		String statement = getInsertQuery(modelObject);
		int lastInsertId = dbResources.executeInsert(statement);
		modelObject.setId(lastInsertId);
		return lastInsertId;
	}

	public int deleteModelObject(T modelObject) {
		String statement = getDeleteQuery(modelObject);
		int affectedRows = dbResources.executeUpdate(statement);
		return affectedRows;
	}

	public T findModelObject(int id) {
		ResultSet resultSet = null;
		T modelObject = null;
		String query = getSelectByIdQuery(id);
		resultSet = dbResources.executeStatement(query);
		try {
			if(resultSet.next()) {
				modelObject = createModelObject(resultSet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return modelObject;
	}

	public boolean updateModelObject(T modelObject) {
		String query = getUpdateQuery(modelObject);
		return dbResources.executeUpdate(query) == 1? true:false;
	}

	public Set<T> selectModelObjects() {
		ResultSet resultSet = null;
		T modelObject;
		Set<T> modelObjects = new HashSet<>();
		String query = getSelectByUserIdQuery();
		resultSet = dbResources.executeStatement(query);
		try {
			while (resultSet.next()) {
				modelObject = createModelObject(resultSet);
				modelObjects.add(modelObject);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return modelObjects;
	}
	
	public int insertOrUpdateModelObject(T modelObject) {
		int lastInsertId = 0;
		if(modelObject.getId() != 0) {
			updateModelObject(modelObject);
		} else {
			lastInsertId = insertModelObject(modelObject);
		}
		return lastInsertId;
	}
	
	abstract User getUser();
	abstract String getTableName();
	abstract String getValuesString(T modelObject);
	abstract String getColumnNames();
	abstract String getColumnValuePairs(T modelObject);

	String getInsertQuery(T modelObject) {
		return "INSERT INTO " +getTableName()+" VALUES (null,"+getValuesString(modelObject)+")";
	}

	String getDeleteQuery(T modelObject) {
		return "DELETE FROM "+getTableName()+" WHERE ID='"+modelObject.getId()+"'";
	}

	String getSelectByIdQuery(int id) {
		return "SELECT * FROM "+getTableName()+" WHERE ID='"+id+"'";
	}

	String getUpdateQuery(T modelObject) {
		return "UPDATE "+getTableName()+" SET "+getColumnValuePairs(modelObject)+" WHERE ID='"+modelObject.getId()+"'";
	}

	String getSelectByUserIdQuery() {
		return "SELECT * FROM "+getTableName()+" WHERE USER_ID='"+getUser().getId()+"'";
	}

	abstract T createModelObject(ResultSet resultSet) throws SQLException;
}
