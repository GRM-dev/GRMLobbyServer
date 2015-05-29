/**
 * 
 */
package pl.grm.sconn.commands.base;

import pl.grm.sconn.ServerMain;
import pl.grm.sconn.commands.CommandType;
import pl.grm.sconn.commands.Commands;
import pl.grm.sconn.commands.IBaseCommand;
import pl.grm.sconn.connection.Connection;

/**
 * @author Levvy055
 *
 */
public class SAYCommand implements IBaseCommand {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pl.grm.sconn.commands.ICommand#execute(pl.grm.sconn.commands.Commands,
	 * java.lang.String, pl.grm.sconn.commands.CommandType,
	 * pl.grm.sconn.connection.Connection)
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
