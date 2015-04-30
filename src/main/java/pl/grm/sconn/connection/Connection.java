package pl.grm.sconn.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import pl.grm.sconn.CLogger;

public class Connection implements Runnable {
	private int ID;
	private int port;
	private InputStream is;
	private OutputStream os;
	private boolean connected;
	private boolean initialized;
	private Socket socket;

	public Connection(int id, Socket socket) {
		this.ID = id;
		this.socket = socket;
		this.port = socket.getPort();
	}

	@Override
	public void run() {
		Thread.currentThread().setName(
				"Connection " + this.ID + " on port " + this.port);
		if (!isConnected() && !isInitialized()) {
			setInitialized(true);
			configureConnection();
		}
	}

	public void configureConnection() {
		try {
			setConnected(true);
			CLogger.info("Connected on port " + port);
			is = socket.getInputStream();
			os = socket.getOutputStream();
			String received = receivePacket();
			while (!received.contains("!close")) {
				try {
					if (received != null & received != "") {
						CLogger.info("Server received message: " + received);
						sendPacket(received);
					}
					received = receivePacket();
				} catch (IOException ex) {
					break;
				}
			}
		} catch (IOException e) {
			if (!e.getMessage().contains("socket closed")) {
				e.printStackTrace();
			}
		} finally {
			closeConnection();
			setConnected(false);
			CLogger.info("Disconnected");
		}
	}

	public void sendPacket(String msg) throws IOException {
		PacketParser.sendMessage(msg, os);
	}

	public String receivePacket() throws IOException {
		return PacketParser.receiveMessage(is);
	}

	public void closeConnection() {
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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
