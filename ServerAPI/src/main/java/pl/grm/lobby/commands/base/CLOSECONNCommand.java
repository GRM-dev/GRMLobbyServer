/**
 * 
 */
package pl.grm.lobby.commands.base;

import java.io.IOException;

import pl.grm.lobby.commands.*;
import pl.grm.lobby.connection.*;

/**
 * @author Levvy055
 *
 */
public class CLOSECONNCommand implements IBaseCommand {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pl.grm.lobby.commands.ICommand#execute(pl.grm.lobby.commands.Commands,
	 * java.lang.String, pl.grm.lobby.commands.CommandType,
	 * pl.grm.lobby.connection.Connection)
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
