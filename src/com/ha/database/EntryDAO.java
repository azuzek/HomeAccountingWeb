/**
 * 
 */
package com.ha.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import com.ha.model.Entry;
import com.ha.model.EntryLine;
import com.ha.model.User;

/**
 * @author MLA
 *
 */
public class EntryDAO extends HADAO<Entry> {
	private User user;
	private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

	/* (non-Javadoc)
	 * @see com.ha.database.HADAO#getUser()
	 */
	@Override
	User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	/* (non-Javadoc)
	 * @see com.ha.database.HADAO#getTableName()
	 */
	@Override
	String getTableName() {
		return "HA.ENTRY";
	}

	/* (non-Javadoc)
	 * @see com.ha.database.HADAO#getColumnNames()
	 */
	@Override
	String getColumnNames() {
		return "USER_ID,DATE,DESCRIPTION";
	}

	/* (non-Javadoc)
	 * @see com.ha.database.HADAO#createModelObject(java.sql.ResultSet)
	 */
	@Override
	Entry createModelObject(ResultSet resultSet) throws SQLException {
		Entry entry = new Entry();
		entry.setId(resultSet.getInt("ID"));
		// entry.setUserId(resultSet.getInt("USER_ID"));
		entry.setDate(resultSet.getTimestamp("DATE").toLocalDateTime().toLocalDate());
		entry.setDescription(resultSet.getString("DESCRIPTION"));
		/* */
		EntryLineDAO entryLineDAO = new EntryLineDAO();

		// TODO: See if dbResources2 can be eliminated and the original dbResources be used again.
		// DBResources dbResources2 = new DBResources();
		// dbResources2.setHaDataSource(dbResources.getHaDataSource());
		entryLineDAO.setDbResources(dbResources);
		Set<EntryLine> entryLines = entryLineDAO.selectEntryLinesByEntryId(entry.getId());
		for(EntryLine entryLine : entryLines) {
			if(entryLine.getAction() == 'C') {
				entry.addCredit(entryLine);
			} else if (entryLine.getAction() == 'D') {
				entry.addDebit(entryLine);
			}
		}
		/* */
		// dbResources2.release();
		return entry;
	}

	@Override
	String getValuesString(Entry entry) {;
		return
				entry.getUser().getId()+",'"+
				entry.getDate().format(FORMATTER)+"','"+
				entry.getDescription()+"'";
	}

	@Override
	String getColumnValuePairs(Entry entry) {
		return 
				"USER_ID="+entry.getUser().getId()+
				",DATE='"+entry.getDate().format(FORMATTER)+
				"',DESCRIPTION='"+entry.getDescription()+"'";
	}
}
