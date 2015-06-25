package pl.grm.lobby.server.commands;

import java.util.*;

import pl.grm.lobby.api.ServerMain;
import pl.grm.lobby.server.CLogger;
import pl.grm.lobby.server.commands.base.*;
import pl.grm.lobby.server.connection.Connection;

public class CommandManager {

	private ServerMain serverMain;
	private ArrayList<String> lastCommands;
	private HashMap<Commands, IBaseCommand> baseCommands;
	private HashMap<JSONOperations, IJSONOperation> jsonOperations;

	public CommandManager(ServerMain serverMain) {
		this.serverMain = serverMain;
		lastCommands = new ArrayList<>();
		baseCommands = new HashMap<>();
		init();
	}

	/**
	 * Initialize base commands
	 */
	private void init() {
		baseCommands.put(Commands.CLOSE, new CLOSECommand());
		baseCommands.put(Commands.CLOSECONN, new CLOSECONNCommand());
		baseCommands.put(Commands.CONNECTIONS, new CONNECTIONSCommand());
		baseCommands.put(Commands.ERROR, new ERRORCommand());
		baseCommands.put(Commands.JSON, new JSONCommand());
		baseCommands.put(Commands.LIST, new LISTCommand());
		baseCommands.put(Commands.MSG, new MSGCommand());
		baseCommands.put(Commands.NONE, new NONECommand());
		baseCommands.put(Commands.SAY, new SAYCommand());
		baseCommands.put(Commands.SEND_ALL, new SENDALLCommand());
		baseCommands.put(Commands.START, new STARTCommand());
		baseCommands.put(Commands.STOP, new STOPCommand());
	}

	/**
	 * Executes command
	 * 
	 * @param command
	 *            command to execute
	 * @param args
	 *            arguments of command
	 * @param offset
	 *            if args contains command string set it to false.\n When args
	 *            are just offset than true.
	 * @param cType
	 *            command invoked by server or client
	 * @return true if correctly executed
	 */
	public boolean executeCommand(Commands command, String args, boolean offset, CommandType cType) {
		return executeCommand(command, args, offset, cType, null);
	}

	/**
	 * Executes command
	 * 
	 * @param command
	 *            command to execute
	 * @param args
	 *            arguments of command
	 * @param offset
	 *            if args contains command string set it to false.\n When args
	 *            are just offset than true.
	 * @param cType
	 *            command invoked by server or client
	 * @param connection
	 *            connection on which commend should be executed
	 * @return true if correctly executed
	 */
	public boolean executeCommand(Commands command, String args, boolean offset, CommandType cType,
			Connection connection) {
		if (!offset) {
			args = Commands.getOffset(args);
		}
		return executeCommand(command, args, cType, connection);
	}

	/**
	 * Executes command
	 * 
	 * @param command
	 *            command to execute
	 * @param args
	 *            arguments of command
	 * @param cType
	 *            command invoked by server or client
	 * @param connection
	 *            connection on which commend should be executed
	 * @return true if correctly executed
	 */
	public synchronized boolean executeCommand(Commands command, String args, CommandType cType,
			Connection connection) {
		try {
			System.out.println(command);
			if (cType == CommandType.NONE || !(command.getType() == cType || command.getType() == CommandType.BOTH)
					|| (command.hasToBeOnline() && !serverMain.isRunning())) { return false; }
			CLogger.info("Executing " + command.toString() + " command.");
			IBaseCommand cmm = baseCommands.get(command);
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
