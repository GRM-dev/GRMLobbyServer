/**
 * 
 */
package pl.grm.sconn.commands.base;

import java.io.IOException;
import java.util.Iterator;

import pl.grm.sconn.ServerMain;
import pl.grm.sconn.commands.CommandType;
import pl.grm.sconn.commands.Commands;
import pl.grm.sconn.commands.ICommand;
import pl.grm.sconn.connection.Connection;
import pl.grm.sconn.connection.PacketParser;

/**
 * @author Levvy055
 *
 */
public class SENDALLCommand implements ICommand {

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
				try {
					PacketParser.sendPacket(Commands.MSG.getCommandString() + " " + args, conn.getSocket());
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return true;
	}

}
