package pl.grm.tut.csharp;

import java.io.*;
import java.net.*;

public class Connection extends Thread {
	private int				port;
	private Socket			socket;
	private InputStream		is;
	private OutputStream	os;
	private ServerSocket	serverSocket;
	private boolean			connected;
	private boolean			initialized;
	
	public Connection(int port) {
		this.port = port;
	}
	
	@Override
	public void run() {
		if (!isConnected() && !isInitialized()) {
			setInitialized(true);
			establish();
		}
	}
	
	public void establish() {
		try {
			serverSocket = new ServerSocket(port, 1);
			System.out.println("Server listening on port " + port + " now");
			socket = serverSocket.accept();
			setConnected(true);
			System.out.println("Connected");
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
		byte[] msgLenBytes = new byte[4];
		msgLenBytes[0] = (byte) (msgLen & 0xff);
		msgLenBytes[1] = (byte) ((msgLen >> 8) & 0xff);
		msgLenBytes[2] = (byte) ((msgLen >> 16) & 0xff);
		msgLenBytes[3] = (byte) ((msgLen >> 24) & 0xff);
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
			if (serverSocket != null) {
				serverSocket.close();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
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
	
	public int getPort() {
		return port;
	}
}
