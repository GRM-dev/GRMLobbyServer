/**
 * 
 */
package pl.grm.sconn.commands.base;

import java.util.Iterator;

import pl.grm.sconn.ServerMain;
import pl.grm.sconn.commands.CommandType;
import pl.grm.sconn.commands.Commands;
import pl.grm.sconn.commands.IBaseCommand;
import pl.grm.sconn.connection.Connection;

/**
 * @author Levvy055
 *
 */
public class SENDALLCommand implements IBaseCommand {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pl.grm.sconn.commands.ICommand#execute(pl.grm.sconn.commands.Commands,
	 * java.lang.String, pl.grm.sconn.commands.CommandType,
	 * pl.grm.sconn.connection.Connection)
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
