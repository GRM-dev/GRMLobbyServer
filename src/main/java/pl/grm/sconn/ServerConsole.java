package pl.grm.sconn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import pl.grm.sconn.commands.Commands;

public class ServerConsole implements Runnable {
	private ServerMain serverMain;

	public ServerConsole(ServerMain serverMain) {
		this.serverMain = serverMain;
	}

	@Override
	public void run() {
		Commands command;
		do {
			String commandS = readCommand();
			command = Commands.getCommand(commandS);
			if (command != null) {
				if (command != Commands.CLOSE) {
					serverMain.executeCommand(command, commandS);
				}
			} else {
				break;
			}
		} while (true);
		serverMain.stopServer(null);
	}

	public String readCommand() {
		String commandS = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			commandS = br.readLine();
			return commandS;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
