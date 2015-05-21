/**
 * 
 */
package pl.grm.sconn.commands.base;

import pl.grm.sconn.commands.CommandType;
import pl.grm.sconn.commands.Commands;
import pl.grm.sconn.commands.ICommand;
import pl.grm.sconn.connection.Connection;

/**
 * @author Levvy055
 *
 */
public class CLOSECONNCommand implements ICommand {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pl.grm.sconn.commands.ICommand#execute(pl.grm.sconn.commands.Commands,
	 * java.lang.String, pl.grm.sconn.commands.CommandType,
	 * pl.grm.sconn.connection.Connection)
	 */
	@Override
	public boolean execute(Commands command, String args, CommandType cType, Connection connection) {
		connection.setClosing(true);
		return true;
	}

}
