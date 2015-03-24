package pl.grm.sconn.connection;

import java.io.*;
import java.net.*;
import java.util.*;

import pl.grm.sconn.*;

public class Connector extends Observable implements Runnable {
	private ServerMain				serverMain;
	private int						currentPort		= 0;
	private int						connectionsOnCurrentPort;
	private int						nextID			= 1;
	private Socket					socket;
	private ArrayList<ServerSocket>	serverSockets	= new ArrayList<ServerSocket>();
	
	public Connector(ServerMain serverMain) {
		this.serverMain = serverMain;
	}
	
	@Override
	public void run() {
		Thread.currentThread().setName("Connector");
		while (!serverMain.isStopRequsted()) {
			establishConnection();
		}
	}
	
	public void establishConnection() {
		int port;
		ServerSocket serverSocket;
		try {
			if (serverSockets.size() != 0 && isCurrentPortAvailable()) {
				port = currentPort;
			} else {
				port = getAvailableNextPort();
				serverSocket = new ServerSocket(port, 10);
				serverSockets.add(serverSocket);
			}
			waitForNewConnection(port);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void waitForNewConnection(int port) throws IOException {
		CLogger.info("Server listening on port " + port + " now");
		socket = serverSockets.get(serverSockets.size() - 1).accept();
		CLogger.info("New connection established. " + socket.getInetAddress());
		notifyObservers();
		Connection connection = new Connection(nextID, socket);
		serverMain.addNewConnectionThread(connection);
		connection.start();
		notifyObservers();
	}
	
	public boolean isCurrentPortAvailable() {
		if (connectionsOnCurrentPort < ServerMain.CONNECTIONS_MAX_POOL && currentPort != 0) { return true; }
		return false;
	}
	
	public int getAvailableNextPort() {
		for (int port = ServerMain.START_PORT; port < ServerMain.MAX_PORT; port++) {
			if (available(port)) { return port; }
		}
		return 0;
	}
	
	public boolean available(int port) {
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
