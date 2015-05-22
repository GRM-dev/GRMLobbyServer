package pl.grm.sconn.json;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.json.JSONObject;

import pl.grm.sconn.CLogger;
import pl.grm.sconn.data.User;

public class JsonConverter {

	private static List<String> classNames;

	public static boolean canBeDeserializated(String msg) throws NullPointerException, ClassNotFoundException,
			IOException {
		if (classNames == null) {
			init();
		}
		String type = new JSONObject(msg).getString("type");
		return classNames.contains(type);
	}

	public static DCObject convertToObject(String msg) throws IOException, JsonConvertException {
		try {
			if (JsonConverter.canBeDeserializated(msg)) {
				JSONObject jsonObject = new JSONObject(msg);
				String type = jsonObject.getString("type");
				int action = jsonObject.getInt("action");
				JSONObject jObj = jsonObject.getJSONObject("object");
				Object convObj = getObject(type, jObj);
				return new DCObject(convObj, action);
			}
		}
		catch (NullPointerException e) {
			throw new JsonConvertException(e);
		}
		catch (ClassNotFoundException e) {
			throw new JsonConvertException(e);
		}
		throw new JsonConvertException("Cannot be deserializated");
	}

	private static Object getObject(String type, JSONObject jObj) {
		Object obj = null;
		switch (type) {
			case "User" :
				obj = toUser(jObj);
				break;

			default :
				break;

		}
		return obj;
	}

	private static User toUser(JSONObject obj) {
		try {
			int id = obj.getInt("ID");
			String name = obj.getString("Name");
			int age = obj.getInt("Age");
			String mail = obj.getString("Mail");
			if (id == 0 || name == "" || age == 0 || mail == "") { throw new Exception(
					"Got bad response. One or more Values is null "); }
			return new User(id, name, age, mail);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			CLogger.logException(ex);
			return null;
		}
	}

	public static String toJson(User user) {
		JSONObject obj = new JSONObject();
		obj.put(user.getID() + "", new JSONObject(user));
		String objS = obj.toString();
		return objS;
	}

	@SuppressWarnings("rawtypes")
	private static void init() throws NullPointerException, IOException, ClassNotFoundException {
		classNames = new ArrayList<>();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader == null) { throw new NullPointerException("No class loader"); }
		String path = "pl/grm/sconn/data";
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		List<Class> classes = new ArrayList<Class>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, path));
		}
		for (Class clazz : classes) {
			String name = clazz.getName();
			int i = name.lastIndexOf('.');
			name = name.substring(i + 1);
			classNames.add(name);
		}
	}

	@SuppressWarnings("rawtypes")
	private static List<Class> findClasses(File directory, String path) throws ClassNotFoundException {
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) { return classes; }
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				classes.addAll(findClasses(file, path + "/" + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				Class<?> clazz = Class.forName(path.replace('/', '.') + "."
						+ file.getName().substring(0, file.getName().length() - 6));
				if (clazz.isAssignableFrom(JsonSerializable.class) || clazz.getSuperclass() == JsonSerializable.class) {
					classes.add(clazz);
				}
			}
		}
		return classes;
	}
}
