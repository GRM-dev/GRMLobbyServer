package pl.grm.tut.csharp;

import java.io.*;

public class ServerConsole implements Runnable {
	
	@Override
	public void run() {
		String command = "";
		do {
			command = readCommand(command);
			executeCommand(command);
		}
		while (!command.contains("!stop"));
		System.out.println("Stopping server");
		System.out.println("Connection amount " + ServerMain.connectionThreads.size());
		ServerMain.executor.shutdownNow();
		ServerMain.stopRequsted = true;
		for (Connection connection : ServerMain.connectionThreads) {
			connection.closeConnection();
		}
	}
	
	public String readCommand(String command) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			command = br.readLine();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return command;
	}
	
	private void executeCommand(String command) {
		switch (command) {
			case "!connections" :
				System.out.println(ServerMain.connectionThreads.size());
				break;
			default :
				System.out.println("Bad command");
				break;
		}
	}
}
