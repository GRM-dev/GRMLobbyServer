package pl.grm.sconn.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import pl.grm.sconn.CLogger;

public class PacketParser {

	public static void sendMessage(String msg, OutputStream os)
			throws IOException {
		byte[] msgBytes = msg.getBytes();
		int msgLen = msgBytes.length;
		byte[] msgLenBytes = convertIntToBytes(msgLen);
		os.write(msgLenBytes);
		os.write(msgBytes);
		CLogger.info("Sended: " + msg);
	}

	private static int convertBytesToInt(byte[] lenBytes) {
		return ((lenBytes[3] & 0xff) << 24) | ((lenBytes[2] & 0xff) << 16)
				| ((lenBytes[1] & 0xff) << 8) | (lenBytes[0] & 0xff);
	}

	public static byte[] convertIntToBytes(int number) {
		return new byte[] { (byte) (number & 0xff),
				(byte) ((number >> 8) & 0xff), (byte) ((number >> 16) & 0xff),
				(byte) ((number >> 24) & 0xff) };
	}

	public static String receiveMessage(InputStream is) throws IOException {
		byte[] lenBytes = new byte[4];
		is.read(lenBytes, 0, 4);
		int len = convertBytesToInt(lenBytes);
		byte[] receivedBytes = new byte[len];
		is.read(receivedBytes, 0, len);
		String received = new String(receivedBytes, 0, len);
		return received;
	}

}
