package pl.grm.tut.csharp;

import java.io.*;

public class ServerConsole implements Runnable {
	private ServerMain	serverMain;
	private boolean		stop;
	
	public ServerConsole(ServerMain serverMain) {
		this.serverMain = serverMain;
	}
	
	@Override
	public void run() {
		String command = "";
		do {
			command = readCommand();
			executeCommand(command);
		}
		while (!stop);
		serverMain.stopServer();
	}
	
	public String readCommand() {
		String command = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			command = br.readLine();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return command;
	}
	
	private void executeCommand(String cName) {
		Commands command = Commands.getCommand(cName);
		switch (command) {
			case STOP :
				stop = true;
				break;
			case CONNECTIONS :
				System.out.println(serverMain.getConnectionsAmount());
				break;
			case SEND_ALL :
				serverMain.getConnection(serverMain.getConnectionsAmount() - 1);
				break;
			default :
				System.out.println("Bad command");
				break;
		}
	}
}
