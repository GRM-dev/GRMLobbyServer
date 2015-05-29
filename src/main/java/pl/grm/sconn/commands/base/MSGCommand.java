/**
 * 
 */
package pl.grm.sconn.commands.base;

import java.io.IOException;

import pl.grm.sconn.commands.CommandType;
import pl.grm.sconn.commands.Commands;
import pl.grm.sconn.commands.ICommand;
import pl.grm.sconn.connection.Connection;
import pl.grm.sconn.connection.PacketParser;

/**
 * @author Levvy055
 *
 */
public class MSGCommand implements ICommand {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pl.grm.sconn.commands.ICommand#execute(pl.grm.sconn.commands.Commands,
	 * java.lang.String, pl.grm.sconn.commands.CommandType,
	 * pl.grm.sconn.connection.Connection)
	 */
	@Override
	public boolean execute(Commands command, String args, CommandType cType, Connection conn) throws Exception {
		try {
			PacketParser.sendPacket(Commands.MSG.getCommandString() + " " + args, conn.getSocket());
			return true;
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
