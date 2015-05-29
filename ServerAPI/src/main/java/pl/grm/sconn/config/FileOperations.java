package pl.grm.sconn.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

public class FileOperations {

	public static File getFile(String filename) throws FileNotFoundException {
		ClassLoader cL = FileOperations.class.getClassLoader();
		URL resFile = cL.getResource(filename);
		File file;
		if (resFile != null) {
			file = new File(resFile.getFile());
		} else {
			file = new File(filename);
		}
		if (file == null || !file.exists()) {
			throw new FileNotFoundException("File not found: " + filename);
		}
		return file;
	}
}
