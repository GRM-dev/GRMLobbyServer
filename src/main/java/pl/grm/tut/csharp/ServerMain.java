package pl.grm.tut.csharp;

import java.util.*;
import java.util.concurrent.*;

public class ServerMain {
	public static int				START_PORT				= 4343;
	public static int				MAX_PORT				= 4350;
	public static int				CONNECTIONS_MAX_POOL	= 5;
	private ArrayList<Connection>	connectionThreads		= new ArrayList<Connection>();
	private boolean					stopRequsted			= false;
	private ExecutorService			executor;
	private Thread					serverConsoleThread;
	private Thread					connectorThread;
	private CommandManager			commandManager;
	private static ServerMain		instance;
	
	public ServerMain() {
		commandManager = new CommandManager(this);
	}
	
	public static void main(String[] args) {
		ServerMain.instance = new ServerMain();
		instance.startServer();
	}
	
	private void startServer() {
		System.out.println("Starting  server");
		executor = Executors.newFixedThreadPool(CONNECTIONS_MAX_POOL);
		setupBaseServerThreads();
	}
	
	public void setupBaseServerThreads() {
		serverConsoleThread = new Thread(new ServerConsole(this));
		connectorThread = new Thread(new Connector(this));
		serverConsoleThread.start();
		connectorThread.start();
	}
	
	public void stopServer() {
		System.out.println("Stopping server");
		System.out.println("Connection amount on stop" + connectionThreads.size());
		executor.shutdownNow();
		setStopRequsted(true);
		for (Connection connection : connectionThreads) {
			connection.closeConnection();
		}
		System.exit(0);
	}
	
	public void addNewConnectionThread(Connection connection) {
		connectionThreads.add(connection);
		connection.start();
	}
	
	public Connection getConnection(int id) {
		if (id < connectionThreads.size()) {
			Connection connection = connectionThreads.get(id);
			return connection;
		}
		return null;
	}
	
	public void executeCommand(String command) {
		commandManager.executeCommand(command);
	}
	
	public boolean isStopRequsted() {
		return stopRequsted;
	}
	
	public void setStopRequsted(boolean stopRequsted) {
		this.stopRequsted = stopRequsted;
	}
	
	public int getConnectionsAmount() {
		return connectionThreads.size();
	}
}
