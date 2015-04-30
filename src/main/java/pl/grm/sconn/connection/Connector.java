package pl.grm.sconn.connection;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;

import pl.grm.sconn.CLogger;
import pl.grm.sconn.ServerMain;

public class Connector extends Observable implements Runnable {
	private ServerMain serverMain;
	private SocketManager sManager;
	private static Connector instance;

	private ArrayList<Connection> connectionThreadsList;

	public Connector(ServerMain serverMain) throws IOException {
		this.serverMain = serverMain;
		this.sManager = new SocketManager(serverMain);
		this.connectionThreadsList = new ArrayList<Connection>();
		instance = this;
	}

	@Override
	public void run() {
		SocketManager.startMng(sManager);
		while (serverMain.isRunning()) {
			try {
				waitForNewConnection();
				new Thread(createConnection()).start();
			} catch (IOException e) {
				serverMain.stopServer(e);
			}
		}
	}

	private void waitForNewConnection() throws IOException {
		int port = sManager.getAvailablePort();
		CLogger.info("Server listening on port " + port + " now");
		sSocket = sManager.getActiveSocket;
		Socket socket = sSocket.accept();
		CLogger.info("New connection established. " + socket.getInetAddress());
		setChanged();
		notifyObservers();
	}

	private Connection createConnection() {
		Connection connection = new Connection(++sManager.nextID,
				sManager.socket);
		serverMain.addNewConnectionThread(connection);
		return connection;
	}

	public void stopConnector() {
		sManager.getExecutor().shutdownNow();
		CLogger.info("Stopping server ...\nConnection amount on stop "
				+ connectionThreadsList.size());
		for (Connection connection : connectionThreadsList) {
			connection.closeConnection();
		}
		try {
			Thread.sleep(1000l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		connectionThreadsList.clear();
		sManager.setExecutor(null);
	}

	public void addNewConnectionThread(Connection connection) {
		connectionThreadsList.add(connection);
	}

	public Connection getConnection(int id) {
		if (id < connectionThreadsList.size()) {
			Connection connection = connectionThreadsList.get(id);
			return connection;
		}
		return null;
	}

	public static int getConnectionsAmount() {
		return instance.connectionThreadsList.size();
	}

	public ArrayList<Connection> getConnectionThreadsList() {
		return connectionThreadsList;
	}
}
