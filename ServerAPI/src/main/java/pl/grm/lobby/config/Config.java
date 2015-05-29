package pl.grm.lobby.config;

import java.io.*;
import java.util.HashMap;

import org.ini4j.*;

import pl.grm.lobby.*;
import pl.grm.lobby.api.ServerMain;

public class Config extends HashMap<Property, String> {
	private static final long serialVersionUID = 1L;
	private static Config config;
	private static String[] SECTION_NAME = {"database", "gui", "server"};

	public static void loadConfig() {
		String filename = ServerMain.CONFIG_FILE_NAME;
		try {
			File file = FileOperations.getFile(filename);
			if (file == null || !file.exists()) {
				throw new FileNotFoundException("File not found.");
			}
			config = new Config();
			Wini ini = new Wini(file);
			config.loadProperties(ini);
		} catch (InvalidFileFormatException e) {
			CLogger.logException(e);
			e.printStackTrace();
		} catch (IOException e) {
			CLogger.logException(e);
			e.printStackTrace();
		}
	}

	private void loadProperties(Wini ini) {

	}
}
