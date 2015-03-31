package pl.grm.sconn.commands;

import java.io.*;

import pl.grm.sconn.*;
import pl.grm.sconn.connection.*;

public class CommandManager {
	private ServerMain	serverMain;
	
	public CommandManager(ServerMain serverMain) {
		this.serverMain = serverMain;
	}
	
	public void executeCommand(String cName) {
		Commands command = Commands.getCommand(cName);
		switch (command) {
			case STOP :
				serverMain.stopServer();
				break;
			case CONNECTIONS :
				System.out.println(serverMain.getConnectionsAmount());
				break;
			case SEND_ALL :
				sendAll(cName);
				break;
			case CLOSE :
				serverMain.setStopRequsted(true);
			case CLOSECONN :
				serverMain.stopServer();
				System.exit(0);
				break;
			case LIST :
			default :
				System.out.println("Bad command");
				break;
		}
	}
	
	private void sendAll(String msg) {
		if (serverMain.getConnectionsAmount() != 0 && msg.length() > 9) {
			msg = msg.substring(9);
			int id = 0;
			Connection connection = serverMain.getConnection(id);
			while (connection != null) {
				try {
					connection.sendPacket(msg);
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				id++;
				connection = serverMain.getConnection(id);
			}
		}
	}
}
