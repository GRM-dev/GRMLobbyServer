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
public class SAYCommand implements IBaseCommand {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pl.grm.lobby.commands.ICommand#execute(pl.grm.lobby.commands.Commands,
	 * java.lang.String, pl.grm.lobby.commands.CommandType,
	 * pl.grm.lobby.connection.Connection)
	 */
	@Override
	public boolean execute(Commands command, String args, CommandType cType, Connection connIn) throws Exception {
		String argsT = args.trim();
		int id = Integer.parseInt(argsT.substring(0, argsT.indexOf(' ')));
		Connection connOut = ServerMain.instance.getConnection(id);
		if (connOut != null) {
			ServerMain.instance.getCM().executeCommand(Commands.MSG, argsT, true, cType, connOut);
			return true;
		}
		return false;
	}

}
