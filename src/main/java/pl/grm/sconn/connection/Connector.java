package pl.grm.sconn.connection;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

import pl.grm.sconn.CLogger;
import pl.grm.sconn.ServerMain;

public class Connector extends Observable implements Runnable {

	private ServerMain serverMain;
	private int port;
	private ServerSocket serverSocket;

	public Connector(ServerMain serverMain) throws IOException {
		this.serverMain = serverMain;
		port = ServerMain.EST_PORT;
		if (isAvailable(port)) {
			serverSocket = new ServerSocket(port);
		} else {
			throw new IOException("Port " + port + " not available!");
		}
	}

	@Override
	public void run() {
		Thread.currentThread().setName("Connector");
		while (serverMain.isRunning()) {
			try {
				waitForNewConnection();
			}
			catch (IOException e) {
				e.printStackTrace();
				serverMain.stopServer();
			}
		}
	}

	public void waitForNewConnection() throws IOException {
		CLogger.info("Server listening on port " + port);
		Socket socket = serverSocket.accept();
		int nextID = nextID();
		CLogger.info("New connection established: ID=" + nextID + " localPort="
				+ socket.getInetAddress());
		Connection connection = new Connection(nextID, socket);
		serverMain.addConnection(nextID, connection);
		connection.start();
		setChanged();
		notifyObservers();
	}

	public int nextID() {
		for (int id = ServerMain.START_CONNECTION_ID; id < 100000; id++) {
			if (!serverMain.containsConnection(id)) { return id; }
		}
		return 0;
	}

	public static boolean isAvailable(int port) {
		ServerSocket ss = null;
		DatagramSocket ds = null;
		try {
			ss = new ServerSocket(port);
			ss.setReuseAddress(true);
			ds = new DatagramSocket(port);
			ds.setReuseAddress(true);
			return true;
		}
		catch (IOException e) {}
		finally {
			if (ds != null) {
				ds.close();
			}
			if (ss != null) {
				try {
					ss.close();
				}
				catch (IOException e) {}
			}
		}
		return false;
	}
}
