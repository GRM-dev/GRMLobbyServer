package pl.grm.sconn.connection;

import java.net.ServerSocket;
import java.util.ArrayList;

import pl.grm.sconn.ServerMain;

public class SocketData {
	private boolean active;
	private ServerSocket serverSocket;
	private ArrayList<Connection> connections;

	public SocketData(ServerSocket serverSocket) {
		this.setServerSocket(serverSocket);
		this.connections = new ArrayList<>();
		this.activate();
	}

	public void addConnection(Connection connection) {
		if (isActive()) {
			this.connections.add(connection);
			if (getConnectionsAmount() == ServerMain.CONNECTIONS_MAX_POOL_PER_SOCKET) {
				deactivate();
			}
		}
	}

	public boolean isActive() {
		return this.active;
	}

	public void activate() {
		this.active = true;
	}

	public void deactivate() {
		this.active = false;
	}

	public ServerSocket getServerSocket() {
		return this.serverSocket;
	}

	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public ArrayList<Connection> getConnections() {
		return this.connections;
	}

	public int getConnectionsAmount() {
		return this.connections.size();
	}
}
