/**
 * 
 */
package pl.grm.lobby.server.commands.base;

import java.io.IOException;

import pl.grm.lobby.server.commands.*;
import pl.grm.lobby.server.connection.Connection;
import pl.grm.lobby.server.json.*;

/**
 * @author Levvy055
 *
 */
public class JSONCommand implements IBaseCommand {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pl.grm.lobby.commands.ICommand#execute(pl.grm.lobby.commands.Commands,
	 * java.lang.String, pl.grm.lobby.commands.CommandType,
	 * pl.grm.lobby.connection.Connection)
	 */
	@Override
	public boolean execute(Commands command, String args, CommandType cType, Connection connection)
			throws IOException, JsonConvertException {
		JsonParser.parse(args, connection);
		return true;
	}

}
