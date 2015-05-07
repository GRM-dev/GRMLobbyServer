package pl.grm.sconn.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.json.JSONObject;

import pl.grm.sconn.CLogger;
import pl.grm.sconn.data.User;

public class PacketParser {

	public static void sendUserData(User user, Socket socket)
			throws IOException {
		JSONObject obj = new JSONObject();
		obj.put(user.getID() + "", new JSONObject(user));
		String objS = obj.toString();
		sendMessage(objS, socket);
	}

	public static User receiveUserData(Socket socket) throws IOException {
		sendMessage("!userdata", socket);
		String rec = "";
		while (!rec.startsWith("{\"")) {
			rec = receiveMessage(socket);
		}
		try {
			JSONObject obj = new JSONObject(rec);
			int id = obj.getInt("ID");
			String name = obj.getString("Name");
			int age = obj.getInt("Age");
			String mail = obj.getString("Mail");
			if (id == 0 || name == "" || age == 0 || mail == "") {
				throw new Exception(
						"Got bad response. One or more Values is null ");
			}
			return new User(id, name, age, mail);
		} catch (Exception ex) {
			ex.printStackTrace();
			CLogger.logException(ex);
			return null;
		}
	}

	public static void sendMessage(String msg, Socket socket)
			throws IOException {
		OutputStream os = socket.getOutputStream();
		byte[] msgBytes = msg.getBytes();
		int msgLen = msgBytes.length;
		byte[] msgLenBytes = convertIntToBytes(msgLen);
		if (!socket.isClosed()) {
			os.write(msgLenBytes);
			os.write(msgBytes);
			CLogger.info("Sended: " + msg);
		}
	}

	public static String receiveMessage(Socket socket) throws IOException {
		InputStream is = socket.getInputStream();
		byte[] lenBytes = new byte[4];
		is.read(lenBytes, 0, 4);
		int len = convertBytesToInt(lenBytes);
		byte[] receivedBytes = new byte[len];
		is.read(receivedBytes, 0, len);
		String received = new String(receivedBytes, 0, len);
		return received;
	}

	private static int convertBytesToInt(byte[] lenBytes) {
		return ((lenBytes[3] & 0xff) << 24) | ((lenBytes[2] & 0xff) << 16)
				| ((lenBytes[1] & 0xff) << 8) | (lenBytes[0] & 0xff);
	}

	private static byte[] convertIntToBytes(int number) {
		return new byte[] { (byte) (number & 0xff),
				(byte) ((number >> 8) & 0xff), (byte) ((number >> 16) & 0xff),
				(byte) ((number >> 24) & 0xff) };
	}
}
