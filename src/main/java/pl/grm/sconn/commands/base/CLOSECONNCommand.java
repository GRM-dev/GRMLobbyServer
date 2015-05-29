/**
 * 
 */
package pl.grm.sconn.commands.base;

import java.io.IOException;

import pl.grm.sconn.commands.CommandType;
import pl.grm.sconn.commands.Commands;
import pl.grm.sconn.commands.IBaseCommand;
import pl.grm.sconn.connection.Connection;
import pl.grm.sconn.connection.PacketParser;

/**
 * @author Levvy055
 *
 */
public class CLOSECONNCommand implements IBaseCommand {

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
		if (cType == CommandType.SERVER) {
			try {
				PacketParser.sendPacket(Commands.CLOSECONN.getCommandString(), connection.getSocket());
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		connection.setClosing(true);
		return true;
	}

}
