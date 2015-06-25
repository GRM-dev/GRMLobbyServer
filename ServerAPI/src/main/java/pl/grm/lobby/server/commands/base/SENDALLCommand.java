/**
 * 
 */
package pl.grm.lobby.server.commands.base;

import java.util.Iterator;

import pl.grm.lobby.api.ServerMain;
import pl.grm.lobby.server.commands.*;
import pl.grm.lobby.server.connection.Connection;

/**
 * @author Levvy055
 *
 */
public class SENDALLCommand implements IBaseCommand {

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
		ServerMain serverMain = ServerMain.instance;
		if (serverMain.getConnectionsAmount() != 0 && args != null && args.length() > 0) {
			for (Iterator<Integer> it = serverMain.getConnectionsIDs().iterator(); it.hasNext();) {
				int id = it.next();
				Connection conn = serverMain.getConnection(id);
				serverMain.getCM().executeCommand(Commands.MSG, args, true, cType, conn);
			}
		}
		return true;
	}
}
