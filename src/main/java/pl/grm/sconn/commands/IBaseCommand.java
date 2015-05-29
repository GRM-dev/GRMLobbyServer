/**
 * 
 */
package pl.grm.sconn.commands;

import pl.grm.sconn.connection.Connection;

/**
 * @author Levvy055
 *
 */
public interface IBaseCommand {

	public boolean execute(Commands command, String args, CommandType cType, Connection connection) throws Exception;
}
