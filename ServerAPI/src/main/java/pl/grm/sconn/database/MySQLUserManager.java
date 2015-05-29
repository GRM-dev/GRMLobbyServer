package pl.grm.sconn.database;

import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import pl.grm.sconn.CLogger;
import pl.grm.sconn.data.User;

/**
 * Operates on DB table 'users'
 *
 */
public class MySQLUserManager {
	private Connection connection;
	private MySQLConnector connector;

	/**
	 * Only constructor
	 * 
	 * @param connector
	 */
	public MySQLUserManager(MySQLConnector connector) {
		if (connector == null) {
			throw new InvalidParameterException("Connector is null");
		}
		this.connector = connector;
		this.connection = connector.getConnection();
	}

	/**
	 * Adds user to DB. Check if can be added or there are problems with db
	 * query.
	 * 
	 * @param user
	 *            User to add
	 * @return true if successfully added to db else user exists
	 */
	public boolean addUser(User user, String passwd) {
		try {
			if (userExists(user)) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			CLogger.logException(e);
		}
		DateFormat dF = new SimpleDateFormat("yyyy-MM-dd");
		String date = dF.format(Calendar.getInstance().getTime());
		String insertQuery = "INSERT INTO `javaserver`.`users` "
				+ "(`username`, `password`, `active`, `activated`, `registered`, `access_level`, `mail`) "
				+ "VALUES ('" + user.getName() + "', '" + passwd
				+ "', '0', '0', '" + date + "', '0', '" + user.getMail() + "')";
		Statement statement = null;
		ResultSet rS = null;
		try {
			statement = connection.createStatement();
			statement.execute(insertQuery);
			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			CLogger.logException(ex);
			return false;
		} finally {
			MySQLConnector.close(statement, rS);
		}
	}

	/**
	 * Deletes user from db if exists
	 * 
	 * @param user
	 *            to delete
	 * @return true if user was deleted
	 */
	public boolean deleteUser(User user) {
		String deleteQuery = "DELETE FROM `javaserver`.`users` WHERE username = '"
				+ user.getName() + "'";
		Statement statement = null;
		ResultSet rS = null;
		try {
			if (userExists(user)) {
				statement = connection.createStatement();
				statement.execute(deleteQuery);
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			CLogger.logException(e);
			return false;
		} finally {
			MySQLConnector.close(statement, rS);
		}
	}

	/**
	 * Checks if user exists in db
	 * 
	 * @param user
	 * @return true if exists, otherwise false
	 * @throws SQLException
	 */
	public boolean userExists(User user) throws SQLException {
		return userExists(user.getName());
	}

	/**
	 * Checks if username exists in db
	 * 
	 * @param username
	 *            of user
	 * @return true if exists, otherwise false
	 * @throws SQLException
	 */
	public boolean userExists(String username) throws SQLException {
		SQLException e = null;
		Statement statement = null;
		ResultSet rS = null;
		try {
			String selectQuery = "SELECT username FROM `javaserver`.`users` WHERE username = '"
					+ username + "'";
			statement = connection.createStatement();
			statement.execute(selectQuery);
			rS = statement.getResultSet();
			if (rS.isBeforeFirst()) {
				return true;
			}
		} catch (SQLException ex) {
			e = ex;
		} finally {
			MySQLConnector.close(statement, rS);
			if (e != null) {
				throw e;
			}
		}
		return false;
	}

	/**
	 * Checks if password to specified user is correct
	 * 
	 * @param name
	 *            username of user
	 * @param psswd
	 *            password to compare
	 * @return true if password is correct
	 */
	public boolean isPasswordCorrect(String name, String psswd) {
		Statement statement = null;
		ResultSet rS = null;
		String SQuery = "SELECT username, password FROM `javaserver`.`users` WHERE username = '"
				+ name + "' AND password = '" + psswd + "'";
		try {
			if (userExists(name)) {
				statement = connection.createStatement();
				statement.execute(SQuery);
				rS = statement.getResultSet();
				if (rS.isBeforeFirst()) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			CLogger.logException(e);
		} finally {
			MySQLConnector.close(statement, rS);
		}
		return false;
	}

	/**
	 * It should be invoked on the end of operations before deleting this object
	 */
	public void endOperations() {
		this.connector.disconnect();
	}
}
