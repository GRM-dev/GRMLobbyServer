package pl.grm.sconn.commands;

import java.util.ArrayList;
import java.util.HashMap;

import pl.grm.sconn.CLogger;
import pl.grm.sconn.ServerMain;
import pl.grm.sconn.commands.base.CLOSECONNCommand;
import pl.grm.sconn.commands.base.CLOSECommand;
import pl.grm.sconn.commands.base.CONNECTIONSCommand;
import pl.grm.sconn.commands.base.ERRORCommand;
import pl.grm.sconn.commands.base.JSONCommand;
import pl.grm.sconn.commands.base.LISTCommand;
import pl.grm.sconn.commands.base.MSGCommand;
import pl.grm.sconn.commands.base.NONECommand;
import pl.grm.sconn.commands.base.SAYCommand;
import pl.grm.sconn.commands.base.SENDALLCommand;
import pl.grm.sconn.commands.base.STARTCommand;
import pl.grm.sconn.commands.base.STOPCommand;
import pl.grm.sconn.connection.Connection;

public class CommandManager {

	private ServerMain serverMain;
	private ArrayList<String> lastCommands;
	private HashMap<Commands, ICommand> commands;

	public CommandManager(ServerMain serverMain) {
		this.serverMain = serverMain;
		lastCommands = new ArrayList<>();
		commands = new HashMap<>();
		init();
	}

	/**
	 * Initialize base commands
	 */
	private void init() {
		commands.put(Commands.CLOSE, new CLOSECommand());
		commands.put(Commands.CLOSECONN, new CLOSECONNCommand());
		commands.put(Commands.CONNECTIONS, new CONNECTIONSCommand());
		commands.put(Commands.ERROR, new ERRORCommand());
		commands.put(Commands.JSON, new JSONCommand());
		commands.put(Commands.LIST, new LISTCommand());
		commands.put(Commands.MSG, new MSGCommand());
		commands.put(Commands.NONE, new NONECommand());
		commands.put(Commands.SAY, new SAYCommand());
		commands.put(Commands.SEND_ALL, new SENDALLCommand());
		commands.put(Commands.START, new STARTCommand());
		commands.put(Commands.STOP, new STOPCommand());
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
			ICommand cmm = commands.get(command);
			return cmm.execute(command, args, cType, connection);
		}
		catch (Exception e) {
			CLogger.logException(e);
			return false;
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
