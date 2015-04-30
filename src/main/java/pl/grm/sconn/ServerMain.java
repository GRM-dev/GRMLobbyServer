package pl.grm.sconn;

import java.awt.EventQueue;
import java.io.IOException;
import java.util.Observable;

import pl.grm.sconn.commands.CommandManager;
import pl.grm.sconn.commands.Commands;
import pl.grm.sconn.connection.Connector;
import pl.grm.sconn.gui.ServerGUI;

public class ServerMain extends Observable {
	public static int ESTABLISHER_PORT = 4342;
	public static int ESTABLISHER_PORT_MAX_POOL = 120;
	public static int START_PORT = 4343;
	public static int SOCKETS_POOL = 5;
	public static int CONNECTIONS_MAX_POOL_PER_SOCKET = 20;
	private boolean running = false;

	private Thread serverConsoleThread;
	private Thread connectorThread;
	private CommandManager commandManager;
	private static ServerMain instance;
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
			CLogger.logException(e);
			System.exit(1);
		}
	}

	private void prepareServer() throws IOException {
		connector = new Connector(this);
		serverConsoleThread = new Thread(new ServerConsole(this),
				"Server Console");
		serverConsoleThread.start();
	}

	public void startServer() {
		if (!isRunning()) {
			CLogger.info("Starting server");
			connectorThread = new Thread(connector, "Connector");
			connectorThread.start();
			setRunning(true);
			setChanged();
			notifyObservers();
		}
	}

	public void stopServer(IOException e) {
		if (e != null) {
			e.printStackTrace();
			CLogger.logException(e);
		}
		if (isRunning()) {
			setRunning(false);
			connectorThread.interrupt();
			connectorThread = null;
			connector.stopConnector();
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

	public boolean executeCommand(Commands command) {
		return commandManager.executeCommand(command, null);
	}

	public boolean executeCommand(Commands command, String msg) {
		return commandManager.executeCommand(command, msg);
	}

	public boolean isRunning() {
		return running;
	}

	private void setRunning(boolean running) {
		this.running = running;
	}
}
