package pl.grm.sconn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import pl.grm.sconn.commands.CommandType;
import pl.grm.sconn.commands.Commands;

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
