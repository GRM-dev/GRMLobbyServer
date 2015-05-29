package pl.grm.lobby.json;

import java.io.IOException;

import pl.grm.lobby.api.ServerMain;
import pl.grm.lobby.connection.Connection;

public class JsonParser {

	private static JsonParser instance = new JsonParser();
	private ServerMain serverMain = ServerMain.instance;

	public static void parse(String object, Connection connection) throws IOException, JsonConvertException {
		if (JsonConverter.canBeDeserializated(object)) {
			DCObject obj = JsonConverter.convertToObject(object);
			String name = obj.getClass().getName();
			System.out.println(name + "..................");
			switch (name) {
				case "pl.grm.lobby.data.User" :
					System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
					// instance.parseUser(obj, connection);
					break;

				default :
					break;
			}
		}
	}

}
