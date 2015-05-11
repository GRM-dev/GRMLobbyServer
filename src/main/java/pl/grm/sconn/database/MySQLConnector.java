package pl.grm.sconn.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class to make connection to MySQL Server
 *
 */
public class MySQLConnector {
	private Connection connection;
	private String url;
	private String user;
	private String password;

	public MySQLConnector() {
		url = "jdbc:mysql://localhost";
		user = "javaserver";
		password = "4rjB9B6bn777HyMX";
	}

	/**
	 * Connects to DB
	 * 
	 * @throws SQLException
	 */
	public void connect() throws SQLException {
		if (connection == null)
			connection = DriverManager.getConnection(url, user, password);
	}

	/**
	 * Closes statement and resultset
	 * 
	 * @param statement
	 * @param rS
	 */
	public static void close(Statement statement, ResultSet rS) {
		try {
			if (statement != null) {
				statement.close();
			}
			if (rS != null) {
				rS.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Disconnect from DB
	 */
	public void disconnect() {
		try {
			if (connection != null) {
				connection.close();
				connection = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return {@link Connection}
	 */
	public Connection getConnection() {
		return connection;
	}
}
