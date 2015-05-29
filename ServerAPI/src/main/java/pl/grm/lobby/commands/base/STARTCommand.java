/**
 * 
 */
package pl.grm.lobby.commands.base;

import pl.grm.lobby.api.ServerMain;
import pl.grm.lobby.commands.*;
import pl.grm.lobby.connection.Connection;

/**
 * @author Levvy055
 *
 */
public class STARTCommand implements IBaseCommand {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pl.grm.lobby.commands.ICommand#execute(pl.grm.lobby.commands.Commands,
	 * java.lang.String, pl.grm.lobby.commands.CommandType,
	 * pl.grm.lobby.connection.Connection)
	 */
	@Override
	public boolean execute(Commands command, String args, CommandType cType, Connection connection) throws Exception {
		ServerMain.instance.startServer();
		return true;
	}

}
