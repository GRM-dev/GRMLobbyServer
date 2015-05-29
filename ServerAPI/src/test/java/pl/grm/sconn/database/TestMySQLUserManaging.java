package pl.grm.sconn.database;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.sql.SQLException;

import org.junit.*;

import pl.grm.lobby.data.User;
import pl.grm.lobby.database.*;

public class TestMySQLUserManaging {
	private static MySQLUserManager userDBManager;

	@BeforeClass
	public static void testConnect() {
		MySQLConnector connector = new MySQLConnector();
		try {
			connector.connect();
			userDBManager = new MySQLUserManager(connector);
		} catch (SQLException e) {
			connector.disconnect();
			e.printStackTrace();
		}
	}

	@Test
	public void testUserExistsUser() {
		User user = new User(-1, "test1", 54, "ad@af.a");
		boolean pass = false;
		try {
			pass = userDBManager.userExists(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertThat(pass, equalTo(true));
	}

	@Test
	public void testUserExistsString() {
		boolean pass = false;
		try {
			pass = userDBManager.userExists("test1");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertThat(pass, equalTo(true));
	}

	@Test
	public void testAddUser() {
		User user = new User(-1, "JackA", 54, "ad@af.a");
		boolean pass = false;
		try {
			if (userDBManager.userExists(user)) {
				userDBManager.deleteUser(user);
			}
			pass = userDBManager.addUser(user, "ggg");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertThat(pass, equalTo(true));
		userDBManager.deleteUser(user);
	}

	@Test
	public void testDeleteUser() {
		User user = new User(-1, "JackD", 54, "ggad@af.aas");
		boolean pass = false;
		try {
			if (!userDBManager.userExists(user)) {
				userDBManager.addUser(user, "aaaa");
			}
			if (!userDBManager.userExists(user)) {
				fail("look at addUser and/or exists");
			} else {
				pass = userDBManager.deleteUser(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertThat(pass, equalTo(true));
	}

	@Test
	public void testIsPasswordCorrect() {
		User user = new User(-1, "JackPC", 54, "pp@pf.aas");
		String psswd = "ggqqee24@%#@Q";
		boolean pass = false;
		try {
			if (!userDBManager.userExists(user)) {
				userDBManager.addUser(user, psswd);
			}
			if (userDBManager.userExists(user)) {
				pass = userDBManager.isPasswordCorrect(user.getName(), psswd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		userDBManager.deleteUser(user);
		assertThat(pass, equalTo(true));
	}

	@AfterClass
	public static void testDisconnect() {
		userDBManager.endOperations();
	}
}
