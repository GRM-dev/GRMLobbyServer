package pl.grm.sconn.json;

import java.io.IOException;

import pl.grm.sconn.ServerMain;
import pl.grm.sconn.connection.Connection;

public class JsonParser {

	public static void parse(ServerMain serverMain, String args, Connection connection) throws IOException,
			JsonConvertException {
		Object obj = JsonConverter.convertToObject(args);
		String name = obj.getClass().getName();
		System.out.println(name + "..................");
		switch (name) {
			case "pl.grm.sconn.data.User" :
				System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
				break;

			default :
				break;
		}
	}

}
