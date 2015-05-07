package pl.grm.sconn;

import java.awt.EventQueue;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.grm.sconn.commands.CommandManager;
import pl.grm.sconn.connection.Connection;
import pl.grm.sconn.connection.Connector;
import pl.grm.sconn.gui.ServerGUI;

public class ServerMain extends Observable {
	public static int EST_PORT = 4342;
	public static int CONNECTIONS_MAX_POOL = 50;
	public static int START_CONNECTION_ID = 100;
	private HashMap<Integer, Connection> connections;
	private boolean running = false;
	private ExecutorService executor;
	private Thread serverConsoleThread;
	private Thread connectorThread;
	private CommandManager commandManager;
	public static ServerMain instance;
	private Connector connector;

	public ServerMain() {
		CLogger.initLogger();
		commandManager = new CommandManager(this);
	}

	public static void main(String[] args) {
		Thread.currentThread().setName("Main");
		ServerMain.instance = new ServerMain();
		try {
			instance.prepareServer();
			if (args.length != 0 && args[0].equals("gui")) {
				instance.startGUI();
			} else {
				instance.startServer();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void prepareServer() throws IOException {
		connections = new HashMap<Integer, Connection>();
		connector = new Connector(this);
		serverConsoleThread = new Thread(new ServerConsole(this));
		serverConsoleThread.start();
	}

	public void startServer() {
		if (!isRunning()) {
			CLogger.info("Starting server");
			executor = Executors.newFixedThreadPool(CONNECTIONS_MAX_POOL);
			connectorThread = new Thread(connector);
			connectorThread.start();
			setRunning(true);
			setChanged();
			notifyObservers();
		}
	}

	public void stopServer() {
		if (isRunning()) {
			CLogger.info("Stopping server ...\nConnection amount on stop "
					+ getConnections().size());
			executor.shutdownNow();
			for (Iterator<Integer> it = getConnections().keySet().iterator(); it
					.hasNext();) {
				int id = it.next();
				Connection connection = getConnections().get(id);
				connection.closeConnection();
			}
			getConnections().clear();
			connectorThread.interrupt();
			connectorThread = null;
			executor = null;
			setRunning(false);
			setChanged();
			notifyObservers();
		}
	}

	private void startGUI() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ServerGUI sGUI = new ServerGUI(ServerMain.this);
					connector.addObserver(sGUI);
					addObserver(sGUI);
					sGUI.setCommandManager(commandManager);
					sGUI.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Connection getConnection(int id) {
		if (getConnections().containsKey(id)) {
			Connection connection = getConnections().get(id);
			return connection;
		}
		return null;
	}

	public boolean executeCommand(String command) {
		return commandManager.executeCommand(command);
	}

	public boolean isRunning() {
		return running;
	}

	private void setRunning(boolean running) {
		this.running = running;
	}

	public int getConnectionsAmount() {
		return getConnections().size();
	}

	public HashMap<Integer, Connection> getConnections() {
		return connections;
	}

}
