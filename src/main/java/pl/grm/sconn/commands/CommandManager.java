package pl.grm.sconn.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import pl.grm.sconn.CLogger;
import pl.grm.sconn.ServerMain;
import pl.grm.sconn.connection.Connection;
import pl.grm.sconn.connection.PacketParser;
import pl.grm.sconn.json.JsonParser;

public class CommandManager {

	private ServerMain serverMain;
	private ArrayList<String> lastCommands;

	public CommandManager(ServerMain serverMain) {
		this.serverMain = serverMain;
		lastCommands = new ArrayList<>();
	}

	public boolean executeCommand(Commands command, String msg, boolean offset, CommandType cType) {
		return executeCommand(command, msg, offset, cType, null);
	}

	public boolean executeCommand(Commands command, String msg, boolean offset, CommandType cType, Connection connection) {
		if (!offset) {
			msg = Commands.getOffset(msg);
		}
		return executeCommand(command, msg, cType, connection);
	}

	public synchronized boolean executeCommand(Commands command, String args, CommandType cType, Connection connection) {
		try {
			System.out.println(command);
			if (cType == CommandType.NONE || !(command.getType() == cType || command.getType() == CommandType.BOTH)
					|| (command.hasToBeOnline() && !serverMain.isRunning())) { return false; }
			CLogger.info("Executing " + command.toString() + " command.");
			switch (command) {
				case STOP :
					serverMain.stopServer();
					break;
				case CONNECTIONS :
					System.out.println(serverMain.getConnectionsAmount());
					break;
				case SEND_ALL :
					sendAll(args);
					break;
				case CLOSE :
					serverMain.stopServer();
					System.exit(0);
					break;
				case CLOSECONN :
					connection.setClosing(true);
					break;
				case LIST :
					break;
				case START :
					serverMain.startServer();
					break;
				case NONE :
					return false;
				case ERROR :
					return false;
				case JSON :
					JsonParser.parse(args, connection);
					break;
				case MSG :
					break;
				case SAY :
					;
					break;
				default :
					System.out.println("Bad command");
					return false;
			}
			return true;
		}
		catch (Exception e) {
			CLogger.logException(e);
			return false;
		}
	}

	private void sendAll(String msg) {
		if (serverMain.getConnectionsAmount() != 0 && msg != null && msg.length() > 0) {
			for (Iterator<Integer> it = serverMain.getConnectionsIDs().iterator(); it.hasNext();) {
				int id = it.next();
				Connection connection = serverMain.getConnection(id);
				try {
					PacketParser.sendPacket(Commands.MSG.getCommandString() + " " + msg, connection.getSocket());
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public void addCommandToList(String input) {
		if (lastCommands.size() == 100) {
			lastCommands.remove(0);
		}
		lastCommands.add(input);
	}

	public String getLastCommand() {
		if (!lastCommands.isEmpty()) { return lastCommands.get(lastCommands.size() - 1); }
		return "";
	}

	public boolean wasExecuted(String input) {
		if (input != null && input != "" && lastCommands.contains(input)) { return true; }
		return false;
	}

	public String getPreviousCommand(String input) {
		if (wasExecuted(input)) {
			int i = lastCommands.indexOf(input);
			if (i != 0) { return lastCommands.get(i - 1); }
		}
		return "";
	}

	public boolean hasNextCommand(String input) {
		if (wasExecuted(input) && lastCommands.indexOf(input) < lastCommands.size() - 1) { return true; }
		return false;
	}

	public String getNextCommand(String input) {
		if (hasNextCommand(input)) {
			int i = lastCommands.indexOf(input);
			return lastCommands.get(i + 1);
		}
		return "";
	}
}
