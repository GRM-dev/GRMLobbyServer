package pl.grm.sconn.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import pl.grm.sconn.CLogger;
import pl.grm.sconn.ServerMain;
import pl.grm.sconn.data.User;

public class Connection extends Thread {
	private int ID;
	private int port;
	private InputStream is;
	private OutputStream os;
	private boolean connected;
	private boolean initialized;
	private Socket socket;
	private User user;

	public Connection(int id, Socket socket) {
		this.ID = id;
		this.socket = socket;
		this.port = socket.getPort();
		this.setName("Connection " + id + " on port " + port);
	}

	@Override
	public void run() {
		if (!isConnected() && !isInitialized()) {
			try {
				configureConnection();
				setInitialized(true);
				String received = "";
				// received=PacketParser.receiveMessage(is);
				user = PacketParser.receiveUserData(is, os);
				System.out.println(user.getName());
				while (!received.contains("!close")) {
					if (received != null & received != "") {
						CLogger.info("Server received message: " + received);
						PacketParser.sendMessage(received, os);
					}
					received = PacketParser.receiveMessage(is);
					if (received.length() > 0
							&& ServerMain.instance.executeCommand(received)) {
						PacketParser.sendMessage("Executed", os);
					}
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				closeConnection();
				setConnected(false);
				CLogger.info("Disconnected");
			}
		}
	}

	public void configureConnection() throws IOException {
		try {
			setConnected(true);
			CLogger.info("Connected on port " + port);
			is = socket.getInputStream();
			os = socket.getOutputStream();

		} catch (IOException e) {
			if (!e.getMessage().contains("socket closed")) {
				e.printStackTrace();
			}
			throw new IOException(e.getMessage());
		}
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

	public InputStream getIs() {
		return is;
	}

	public OutputStream getOs() {
		return os;
	}

	public Socket getSocket() {
		return socket;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
