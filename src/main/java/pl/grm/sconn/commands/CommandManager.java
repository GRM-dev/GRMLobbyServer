package pl.grm.sconn.commands;

import java.io.IOException;
import java.util.ArrayList;

import pl.grm.sconn.ServerMain;
import pl.grm.sconn.connection.Connection;
import pl.grm.sconn.connection.Connector;

public class CommandManager {
	private ServerMain serverMain;
	private ArrayList<String> lastCommands;

	public CommandManager(ServerMain serverMain) {
		this.serverMain = serverMain;
		lastCommands = new ArrayList<>();
	}

	public boolean executeCommand(Commands command, String msg) {
		switch (command) {
		case START:
			serverMain.startServer();
			break;
		case STOP:
			serverMain.stopServer(null);
			break;
		case CONNECTIONS:
			System.out.println(Connector.getConnectionsAmount());
			break;
		case SEND_ALL:
			sendAll(msg);
			break;
		case CLOSE:
			serverMain.stopServer(null);
			System.exit(0);
			break;
		case CLOSECONN:
			serverMain.getConnection(0).closeConnection();
			break;
		case LIST:
			break;
		case NONE:
			break;
		default:
			System.out.println("Bad command");
			return false;
		}
		return true;
	}

	private void sendAll(String msg) {
		if (serverMain.getConnectionsAmount() != 0 && msg.length() > 9) {
			msg = msg.substring(9);
			int id = 0;
			Connection connection = serverMain.getConnection(id);
			while (connection != null) {
				try {
					connection.sendPacket(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
				id++;
				connection = serverMain.getConnection(id);
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
		if (!lastCommands.isEmpty()) {
			return lastCommands.get(lastCommands.size() - 1);
		}
		return "";
	}

	public boolean wasExecuted(String input) {
		if (input != null && input != "" && lastCommands.contains(input)) {
			return true;
		}
		return false;
	}

	public String getPreviousCommand(String input) {
		if (wasExecuted(input)) {
			int i = lastCommands.indexOf(input);
			if (i != 0) {
				return lastCommands.get(i - 1);
			}
		}
		return "";
	}

	public boolean hasNextCommand(String input) {
		if (wasExecuted(input)
				&& lastCommands.indexOf(input) < lastCommands.size() - 1) {
			return true;
		}
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
