package com.ha.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.ha.model.Category;
import com.ha.model.RootCategory;
import com.ha.model.User;
import com.ha.model.AccountType;

public class CategoryDAO extends HADAO<Category> {
	private User user;
	private Category category;
	
	
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	String getTableName() {
		return "HA.CATEGORY";
	}

	@Override
	String getValuesString(Category category) {
		return 
			category.getUserId()+","+
			category.getParentId()+",'"+
			category.getType().getCode()+"',"+
			(category.isFixed()? 1: 0)+",'"+
			category.getName()+"'";
	}

	@Override
	String getColumnNames() {
		return "USER_ID,PARENT_ID,TYPE,FIXED,NAME";
	}

	@Override
	String getColumnValuePairs(Category category) {
		return 
				"USER_ID="+category.getUserId()+
				",PARENT_ID='"+category.getParentId()+
				"',TYPE='"+category.getType()+
				"',FIXED='"+(category.isFixed()? 1: 0)+
				"',NAME='"+category.getName()+"'";
	}

	@Override
	Category createModelObject(ResultSet resultSet) throws SQLException {
		Category category = null;
		if(resultSet.getBoolean("FIXED")) {
			category = new RootCategory();
		} else {
			category = new Category();
		}
		category.setFixed(resultSet.getBoolean("FIXED"));
		category.setId(resultSet.getInt("ID"));
		category.setUserId(resultSet.getInt("USER_ID"));
		category.setParentId(resultSet.getInt("PARENT_ID"));
		char code = resultSet.getString("TYPE").charAt(0);
		category.setType(AccountType.fromCode(code));
		category.setName(resultSet.getString("NAME"));
		return category;
	}
	
	public Set<Category> getCategories() {
		Set<Category> structuredCategories = new HashSet<>();
		Set<Category> allCategories = selectModelObjects();
		structuredCategories.addAll(allCategories.stream().filter(p -> p.getParentId() == 0).collect(Collectors.toSet()));
		for(Category category : structuredCategories) {
			addChildren(category, allCategories);
		}
		return structuredCategories;
	}

	private void addChildren(Category category, Set<Category> allCategories) {
		for(Category child : allCategories.stream().filter(p -> p.getParentId() == category.getId()).collect(Collectors.toSet())) {
			category.addChild(child);
			addChildren(child, allCategories);
		}
	}

	public Set<Category> getParents() {
		Set<Category> parents = new HashSet<>();
		ResultSet resultSet = null;
		Category category = null;
		String query = 
				"select id,user_id,parent_id,type,fixed,name "+
				"from ha.category where user_id="+getUser().getId()+
				" and parent_id is null";
		resultSet = dbResources.executeStatement(query);
		try {
			while (resultSet.next()) {
				category = createModelObject(resultSet);
				parents.add(category);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbResources.release();
		}
		return parents;
	}
	
	public Set<Category> getChildren() {
		Set<Category> children = new HashSet<>();
		ResultSet resultSet = null;
		Category child = null;
		String query = 
				"select id,user_id,parent_id,type,fixed,name "+
				"from ha.category where user_id="+getUser().getId()+
				" and parent_id="+category.getId();
		resultSet = dbResources.executeStatement(query);
		try {
			while (resultSet.next()) {
				child = createModelObject(resultSet);
				children.add(child);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbResources.release();
		}
		return children;
	}
	
	public Category findParent(Category category) {
		return this.findModelObject(category.getParentId());
	}
	
	public Category findRoot(Category category) {
		Category rootCandidate = this.findParent(category);
		if(rootCandidate == null) {
			return category;
		} else {
			return findRoot(rootCandidate);
		}
	}
}
