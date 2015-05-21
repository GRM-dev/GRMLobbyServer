/**
 * 
 */
package pl.grm.sconn.commands.base;

import java.io.IOException;

import pl.grm.sconn.commands.CommandType;
import pl.grm.sconn.commands.Commands;
import pl.grm.sconn.commands.ICommand;
import pl.grm.sconn.connection.Connection;
import pl.grm.sconn.json.JsonConvertException;
import pl.grm.sconn.json.JsonParser;

/**
 * @author Levvy055
 *
 */
public class JSONCommand implements ICommand {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pl.grm.sconn.commands.ICommand#execute(pl.grm.sconn.commands.Commands,
	 * java.lang.String, pl.grm.sconn.commands.CommandType,
	 * pl.grm.sconn.connection.Connection)
	 */
	@Override
	public boolean execute(Commands command, String args, CommandType cType, Connection connection) throws IOException,
			JsonConvertException {
		JsonParser.parse(args, connection);
		return true;
	}

}
