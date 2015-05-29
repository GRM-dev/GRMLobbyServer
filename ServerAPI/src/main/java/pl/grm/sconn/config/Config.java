package pl.grm.sconn.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

import pl.grm.sconn.CLogger;
import pl.grm.sconn.ServerMain;

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
