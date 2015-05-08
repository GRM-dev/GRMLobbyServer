package pl.grm.sconn.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnect {
	private Connection con;
	private String url;
	private String user;
	private String password;

	public MySQLConnect() {
		url = "jdbc:mysql://localhost";
		user = "javaserver";
		password = "4rjB9B6bn777HyMX";
	}

	public void connect() throws SQLException {
		con = DriverManager.getConnection(url, user, password);
	}

	public void disconnect() {
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
