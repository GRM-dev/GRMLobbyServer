package pl.grm.sconn.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.json.JSONObject;

import pl.grm.sconn.CLogger;
import pl.grm.sconn.data.User;

public class PacketParser {

	public static boolean parseJSON(String msg) {
		return false;
		// TODO Auto-generated method stub

	}

	public static void sendUserData(User user, Socket socket) throws IOException {
		JSONObject obj = new JSONObject();
		obj.put(user.getID() + "", new JSONObject(user));
		String objS = obj.toString();
		sendPacket(objS, socket);
	}

	public static User receiveUserData(Socket socket) throws IOException {
		sendPacket("!userdata", socket);
		String rec = "";
		while (!rec.startsWith("{\"")) {
			rec = receivePacket(socket);
		}
		try {
			JSONObject obj = new JSONObject(rec);
			int id = obj.getInt("ID");
			String name = obj.getString("Name");
			int age = obj.getInt("Age");
			String mail = obj.getString("Mail");
			if (id == 0 || name == "" || age == 0 || mail == "") { throw new Exception(
					"Got bad response. One or more Values is null "); }
			return new User(id, name, age, mail);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			CLogger.logException(ex);
			return null;
		}
	}

	public static void sendPacket(String msg, Socket socket) throws IOException {
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		out.write(msg.getBytes());
	}

	public static String receivePacket(Socket socket) throws IOException {
		DataInputStream in = new DataInputStream(socket.getInputStream());
		String str = "";
		byte[] msgB = new byte[1000];
		byte[] lenB = new byte[4];
		boolean isFinished = false;
		int read = 0;
		for (int i = 0; i < 4; i++) {
			lenB[i] = in.readByte();
		}
		int toRead = convertBytesToInt(lenB) - 4;

		while (!isFinished) {

			read = in.read(msgB);
			str += new String(msgB, 0, read);
			System.out.println(str.length() + "|" + toRead + " " + str);
			if (str.length() == toRead) {
				isFinished = true;
			}
		}
		return str;
	}

	private static boolean isEmpty(byte[] lenBytes) {
		return lenBytes[0] != 0 || lenBytes[1] != 0 || lenBytes[2] != 0
				|| lenBytes[3] != 0;
	}

	private static int convertBytesToInt(byte[] lenBytes) {
		return ((lenBytes[3] & 0xff) << 24) | ((lenBytes[2] & 0xff) << 16)
				| ((lenBytes[1] & 0xff) << 8) | (lenBytes[0] & 0xff);
	}

	private static byte[] convertIntToBytes(int number) {
		return new byte[]{(byte) (number & 0xff), (byte) ((number >> 8) & 0xff),
				(byte) ((number >> 16) & 0xff), (byte) ((number >> 24) & 0xff)};
	}
}
