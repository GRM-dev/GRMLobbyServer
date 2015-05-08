package pl.grm.sconn.database;

import java.sql.SQLException;

import org.junit.Test;

public class TestMySQLUserManaging {

	@Test
	public void testConnect() {
		MySQLConnect connect = new MySQLConnect();
		try {
			connect.connect();
		} catch (SQLException e) {
			connect.disconnect();
			e.printStackTrace();
			// fail("Exception " + e.getMessage());
		}
	}

}
