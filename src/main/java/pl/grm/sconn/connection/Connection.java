package pl.grm.sconn.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import pl.grm.sconn.CLogger;
import pl.grm.sconn.ServerMain;
import pl.grm.sconn.commands.CommandType;
import pl.grm.sconn.commands.Commands;
import pl.grm.sconn.data.User;
import pl.grm.sconn.gui.ConnectionTab;
import pl.grm.sconn.json.JsonConvertException;

public class Connection extends Thread {

	private final int ID;
	private int port;
	private InputStream is;
	private OutputStream os;
	private boolean connected;
	private boolean initialized;
	private Socket socket;
	private User user;
	private ConnectionTab tab;
	private boolean closing;

	public Connection(int id, Socket socket) {
		this.ID = id;
		this.socket = socket;
		this.port = socket.getPort();
		this.setName("ID: " + id);
		this.setClosing(false);
		this.setTab(new ConnectionTab(this));
		ServerMain.instance.notifyObservers();
	}

	@Override
	public void run() {
		if (!isConnected() && !isInitialized()) {
			try {
				configureConnection();
				setInitialized(true);
				String received = "";
				user = PacketParser.receiveUserData(socket);
				CLogger.info("Welcome " + user.getName() + "!");
				tab.fillUP();
				while (isConnected()) {
					received = null;
					received = PacketParser.receivePacket(socket);
					if (received != null && received.length() > 0) {
						CLogger.info("Received packet: " + received);
						Commands command = Commands.getCommand(received);
						if (ServerMain.instance.getCM().executeCommand(command, received, false, CommandType.CLIENT,
								this)) {
							CLogger.info("Command " + command.toString() + " executed on connection " + ID);
						} else {
							CLogger.info("Command not executed on connection " + ID);
						}
					}
				}
			}
			catch (IOException ex) {
				if (!isClosing()) {
					ex.printStackTrace();
				}
			}
			catch (JsonConvertException e) {
				e.printStackTrace();
			}
			finally {
				closeConnection();
				if (isClosing()) {
					CLogger.info("Succesfully disconnected client with ID: " + ID);
				} else {
					CLogger.info("Connection " + ID + " Interrupted!");
				}
				ServerMain.instance.destroyConnection(ID);
			}
		}
	}

	public void configureConnection() throws IOException {
		try {
			CLogger.info("Connected on port " + port);
			is = socket.getInputStream();
			os = socket.getOutputStream();
			setConnected(true);
		}
		catch (IOException e) {
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
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		setConnected(false);
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

	private void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ConnectionTab getTab() {
		return tab;
	}

	private void setTab(ConnectionTab tab) {
		this.tab = tab;
	}

	public boolean isClosing() {
		return this.closing;
	}

	public void setClosing(boolean closing) {
		this.closing = closing;
	}
}
