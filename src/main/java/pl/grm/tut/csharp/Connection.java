package pl.grm.tut.csharp;

import java.io.*;
import java.net.*;

public class Connection extends Thread {
	private int				ID;
	private int				port;
	private InputStream		is;
	private OutputStream	os;
	private boolean			connected;
	private boolean			initialized;
	private Socket			socket;
	
	public Connection(int id, Socket socket) {
		this.ID = id;
		this.socket = socket;
		this.port = socket.getPort();
	}
	
	@Override
	public void run() {
		if (!isConnected() && !isInitialized()) {
			setInitialized(true);
			configureConnection();
		}
	}
	
	public void configureConnection() {
		try {
			setConnected(true);
			System.out.println("Connected on port " + port);
			is = socket.getInputStream();
			os = socket.getOutputStream();
			String received = receivePacket();
			while (!received.contains("!close")) {
				try {
					System.out.println("Server received message: " + received);
					sendPacket(received);
					received = receivePacket();
				}
				catch (IOException ex) {
					break;
				}
			}
		}
		catch (IOException e) {
			if (!e.getMessage().contains("socket closed")) {
				e.printStackTrace();
			}
		}
		finally {
			closeConnection();
			setConnected(false);
			System.out.println("Disconnected");
		}
	}
	
	public void sendPacket(String msg) throws IOException {
		byte[] msgBytes = msg.getBytes();
		int msgLen = msgBytes.length;
		byte[] msgLenBytes = convertIntToBytes(msgLen);
		os.write(msgLenBytes);
		os.write(msgBytes);
		System.out.println("Sended: " + msg);
	}
	
	public String receivePacket() throws IOException {
		byte[] lenBytes = new byte[4];
		is.read(lenBytes, 0, 4);
		int len = (((lenBytes[3] & 0xff) << 24) | ((lenBytes[2] & 0xff) << 16)
				| ((lenBytes[1] & 0xff) << 8) | (lenBytes[0] & 0xff));
		byte[] receivedBytes = new byte[len];
		is.read(receivedBytes, 0, len);
		String received = new String(receivedBytes, 0, len);
		return received;
	}
	
	public void closeConnection() {
		try {
			if (socket != null) {
				socket.close();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] convertIntToBytes(int integer) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (integer & 0xff);
		bytes[1] = (byte) ((integer >> 8) & 0xff);
		bytes[2] = (byte) ((integer >> 16) & 0xff);
		bytes[3] = (byte) ((integer >> 24) & 0xff);
		return bytes;
	}
	
	public int getID() {
		return ID;
	}

	public int getPort() {
		return port;
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	
	public boolean isInitialized() {
		return initialized;
	}
	
	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}
}
