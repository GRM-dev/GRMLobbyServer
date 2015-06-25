/**
 * 
 */
package pl.grm.lobby.server.commands;

import pl.grm.lobby.server.connection.Connection;

/**
 * @author Levvy055
 *
 */
public interface IBaseCommand {

	public boolean execute(Commands command, String args, CommandType cType, Connection connection) throws Exception;
}
