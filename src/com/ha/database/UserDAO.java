/**
 * 
 */
package com.ha.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ha.model.Account;
import com.ha.model.Category;
import com.ha.model.User;

/**
 * @author MLA
 *
 */
public class UserDAO extends HADAO<User> {
	
	public User findUser(String userId) {
		ResultSet resultSet = null;
		User user = null;
		String query = "SELECT * FROM HA.USER WHERE USERID='"+userId+"'";
		resultSet = dbResources.executeStatement(query);
		try {
			if(resultSet.next()) {
				user = createModelObject(resultSet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	public void populateAccounts(User user) {
		Category category = null;
		if(user.getCategories().isEmpty()) {
			populateCategories(user);
		}
		AccountDAO accountDAO = new AccountDAO();
		accountDAO.setDbResources(dbResources);
		accountDAO.setUser(user);
		user.setAccounts(accountDAO.selectModelObjects());
		for(Account account : user.getAccounts()) {
			category = Category.findCategory(user.getCategories(), account.getCategory().getId());
			category.addAccount(account);
		}
	}
	
	public void populateCategories(User user) {
		CategoryDAO categoryDAO = new CategoryDAO();
		categoryDAO.setDbResources(dbResources);
		categoryDAO.setUser(user);
		user.setCategories(categoryDAO.getCategories());
	}

	@Override
	User getUser() {
		// TODO: Analyze if returning null makes sense.
		return null;
	}

	@Override
	String getTableName() {
		return "HA.USER";
	}

	@Override
	String getValuesString(User user) {
		return
			user.getUserId()+","+
			user.getPassword();
	}

	@Override
	String getColumnNames() {
		return "USERID,PASSWORD";
	}

	@Override
	User createModelObject(ResultSet resultSet) throws SQLException {
		User user = new User();
		user.setId(resultSet.getInt("ID"));
		user.setUserId(resultSet.getString("USERID"));
		user.setPassword(resultSet.getString("PASSWORD"));
		return user;
	}

	@Override
	String getColumnValuePairs(User user) {
		return 
				"USERID='"+user.getUserId()+
				"',PASSWORD="+user.getPassword()+"'";
	}
}
