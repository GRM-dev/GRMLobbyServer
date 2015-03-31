package pl.grm.sconn;

import java.awt.*;
import java.util.*;
import java.util.concurrent.*;

import pl.grm.sconn.commands.*;
import pl.grm.sconn.connection.*;
import pl.grm.sconn.gui.*;

public class ServerMain {
	public static int				EST_PORT				= 4342;
	public static int				START_PORT				= 4343;
	public static int				MAX_PORT				= 4350;
	public static int				CONNECTIONS_MAX_POOL	= 5;
	private ArrayList<Connection>	connectionThreadsList;
	private boolean					stopRequsted			= false;
	private ExecutorService			executor;
	private Thread					serverConsoleThread;
	private Thread					connectorThread;
	private CommandManager			commandManager;
	private static ServerMain		instance;
	private boolean					guiActive;
	private Connector				connector;
	
	public ServerMain() {
		commandManager = new CommandManager(this);
	}
	
	public static void main(String[] args) {
		Thread.currentThread().setName("Main");
		ServerMain.instance = new ServerMain();
		instance.prepareServer();
		if (args.length != 0 && args[0].equals("gui")) {
			instance.setGuiActive(true);
			instance.startGUI();
		} else {
			instance.startServer();
		}
	}
	
	private void prepareServer() {
		CLogger.initLogger();
		executor = Executors.newFixedThreadPool(CONNECTIONS_MAX_POOL);
		connectionThreadsList = new ArrayList<Connection>();
		serverConsoleThread = new Thread(new ServerConsole(this));
		connector = new Connector(this);
		
		serverConsoleThread.start();
	}
	
	public void startServer() {
		CLogger.info("Starting server");
		connectorThread = new Thread(connector);
		connectorThread.start();
	}
	
	public void stopServer() {
		CLogger.info("Stopping server ...\nConnection amount on stop "
				+ connectionThreadsList.size());
		executor.shutdownNow();
		setStopRequsted(true);
		for (Connection connection : connectionThreadsList) {
			connection.closeConnection();
		}
		connectionThreadsList.clear();
		connectorThread.interrupt();
		connectorThread = null;
		executor = null;
	}
	
	private void startGUI() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ServerGUI sGUI = new ServerGUI(ServerMain.this);
					connector.addObserver(sGUI);
					sGUI.setVisible(true);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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
		return connectionThreadsList.size();
	}
	
	public boolean isGuiActive() {
		return guiActive;
	}
	
	public void setGuiActive(boolean guiActive) {
		this.guiActive = guiActive;
	}
}
