package pl.grm.tut.csharp;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ServerMain {
	public static int					START_PORT			= 4343;
	public static int					MAX_PORT			= 4360;
	public static int					THREAD_MAX_POOL		= 5;
	public static ArrayList<Connection>	connectionThreads	= new ArrayList<Connection>();
	public static boolean				stopRequsted		= false;
	public static ExecutorService		executor;
	private static Thread				serverConsole;
	private static Thread				executorThread;
	private static Thread				establisherThread;
	
	public static void main(String[] args) {
		System.out.println("Starting  server");
		executor = Executors.newFixedThreadPool(THREAD_MAX_POOL);
		setupThreads();
		serverConsole.start();
		establisherThread.start();
		executorThread.start();
	}
	
	public static void setupThreads() {
		ServerConsole scr = new ServerConsole();
		serverConsole = new Thread(scr);
		executorThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!stopRequsted) {
					for (Connection conn : connectionThreads) {
						if (!conn.isAlive() && !conn.isConnected() && !conn.isInitialized()) {
							executor.execute(conn);
						}
					}
					try {
						Thread.sleep(100l);
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		establisherThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!stopRequsted) {
					boolean isFull = true;
					for (Connection conn : connectionThreads) {
						if (!conn.isConnected()) {
							isFull = false;
						}
					}
					if (isFull) {
						createNewConnectionThread();
					}
					try {
						Thread.sleep(100l);
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	private static void createNewConnectionThread() {
		int port = getAvailableNextPort();
		Connection connection = new Connection(port);
		connectionThreads.add(connection);
	}
	
	public static int getAvailableNextPort() {
		for (int port = START_PORT; port < MAX_PORT; port++) {
			if (available(port)) { return port; }
		}
		return 0;
	}
	
	public static boolean available(int port) {
		ServerSocket ss = null;
		DatagramSocket ds = null;
		try {
			ss = new ServerSocket(port);
			ss.setReuseAddress(true);
			ds = new DatagramSocket(port);
			ds.setReuseAddress(true);
			return true;
		}
		catch (IOException e) {}
		finally {
			if (ds != null) {
				ds.close();
			}
			if (ss != null) {
				try {
					ss.close();
				}
				catch (IOException e) {}
			}
		}
		return false;
	}
}
