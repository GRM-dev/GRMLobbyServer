package pl.grm.lobby.server;

import java.io.*;

import pl.grm.lobby.api.ServerMain;
import pl.grm.lobby.server.commands.*;

public class ServerConsole implements Runnable {

	private ServerMain serverMain;
	private boolean stop;

	public ServerConsole(ServerMain serverMain) {
		this.serverMain = serverMain;
	}

	@Override
	public void run() {
		Thread.currentThread().setName("Server Console");
		String command = "";
		do {
			command = readCommand();
			serverMain.getCM().executeCommand(Commands.getCommand(command), command, false, CommandType.SERVER);
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
}
