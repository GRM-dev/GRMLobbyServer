package pl.grm.sconn.json;

import java.io.IOException;

import pl.grm.sconn.ServerMain;
import pl.grm.sconn.connection.Connection;

public class JsonParser {

	private static JsonParser instance = new JsonParser();
	private ServerMain serverMain = ServerMain.instance;

	public static void parse(String object, Connection connection) throws IOException, JsonConvertException {
		Object obj = JsonConverter.convertToObject(object);
		String name = obj.getClass().getName();
		System.out.println(name + "..................");
		switch (name) {
			case "pl.grm.sconn.data.User" :
				System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
				instance.parseUser(obj, connection);
				break;

			default :
				break;
		}
	}

	private void parseUser(Object obj, Connection connection) {
		// TODO Auto-generated method stub

	}

}
