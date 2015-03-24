package pl.grm.sconn;

import java.io.*;

public class ServerConsole implements Runnable {
	private ServerMain	serverMain;
	private boolean		stop;
	
	public ServerConsole(ServerMain serverMain) {
		this.serverMain = serverMain;
	}
	
	@Override
	public void run() {
		Thread.currentThread().setName("Server Console");
		String command = "";
		do {
			command = readCommand();
			serverMain.executeCommand(command);
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
