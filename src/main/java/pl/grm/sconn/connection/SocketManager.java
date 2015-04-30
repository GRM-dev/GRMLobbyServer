package pl.grm.sconn.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.grm.sconn.CLogger;
import pl.grm.sconn.ServerMain;

public class SocketManager implements Runnable {
	private ServerMain serverMain;
	private static Thread sManagerThread;
	private ServerSocket establisherSocket;
	private ExecutorService executor;
	/** Port, ServerSocket, Connection */
	private HashMap<Integer, SocketData> serverSockets;

	public SocketManager(ServerMain serverMain) throws IOException {
		this.serverMain = serverMain;
		this.serverSockets = new HashMap<Integer, SocketData>();
		this.establisherSocket = new ServerSocket(ServerMain.ESTABLISHER_PORT,
				ServerMain.ESTABLISHER_PORT_MAX_POOL);
	}

	public static void startMng(SocketManager sManager) {
		sManagerThread = new Thread(sManager, "SocketManager");
		sManagerThread.start();
	}

	@Override
	public void run() {
		setExecutor(Executors
				.newFixedThreadPool(ServerMain.CONNECTIONS_MAX_POOL_PER_SOCKET));
		while (serverMain.isRunning()) {
			try {
				Socket socket = establisherSocket.accept();
				System.out.println("After accept");
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							InputStream is = socket.getInputStream();
							OutputStream os = socket.getOutputStream();
							int port = getAvailablePort();
							String msg = PacketParser.receiveMessage(is);
							PacketParser.sendMessage(
									"CnnPortFrom" + ServerMain.ESTABLISHER_PORT
											+ "To" + port, os);
							CLogger.info(socket.getInetAddress()
									.getHostAddress()
									+ " is connecting to est and received port "
									+ port + " to connect.");

						} catch (IOException e) {
							e.printStackTrace();
							serverMain.stopServer(e);
						}
					}
				}, "Establisher").start();
			} catch (IOException e) {
				serverMain.stopServer(e);
			}
		}
	}

	public boolean isCurrentPortAvailable(Connector connector) {
		if (serverSockets.size() != 0
				&& serverSockets.get(currentServerSocketID) < ServerMain.CONNECTIONS_MAX_POOL_PER_SOCKET
				&& currentPort != 0) {
			return true;
		}
		return false;
	}

	public int checkAndFixPort() throws IOException {
		if (!isCurrentPortAvailable(this)) {
			int port = getAvailableNextPort();
			ServerSocket serverSocket = new ServerSocket(currentPort,
					ServerMain.CONNECTIONS_MAX_POOL_PER_SOCKET);
			serverSockets.put(++currentServerSocketID, serverSocket);
			return port;
		} else {
			return getCurrentPort();
		}
	}

	public int getAvailablePort() {
		Iterator<Integer> it = serverSockets.keySet().iterator();
		while (it.hasNext()) {
			Integer id = it.next();
			SocketData sData = serverSockets.get(id);
			if (sData.isActive()) {
				return sData.getServerSocket().getLocalPort();
			}
		}
		for (int port = ServerMain.START_PORT; port < ServerMain.SOCKETS_POOL; port++) {
			if (isAvailable(port)) {
				return port;
			}
		}
		return 0;
	}

	public boolean isAvailable(int port) {
		ServerSocket ss = null;
		DatagramSocket ds = null;
		try {
			ss = new ServerSocket(port);
			ss.setReuseAddress(true);
			ds = new DatagramSocket(port);
			ds.setReuseAddress(true);
			return true;
		} catch (IOException e) {
		} finally {
			if (ds != null) {
				ds.close();
			}
			if (ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
				}
			}
		}
		return false;
	}

	public ExecutorService getExecutor() {
		return executor;
	}

	public void setExecutor(ExecutorService executor) {
		this.executor = executor;
	}

}