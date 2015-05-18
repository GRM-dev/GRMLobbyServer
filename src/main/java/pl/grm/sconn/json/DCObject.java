package pl.grm.sconn.json;

import pl.grm.sconn.data.User;

/** Deserializabled and converted object */
public class DCObject {

	private Object obj;
	private Class<? extends JsonSerializable> clazz;

	@SuppressWarnings("unchecked")
	DCObject(Object obj) {
		this.obj = obj;
		if (obj.getClass().isAssignableFrom(JsonSerializable.class)
				|| obj.getClass().getSuperclass() == JsonSerializable.class) {
			this.clazz = (Class<? extends JsonSerializable>) obj.getClass();
		} else {
			throw new ClassCastException(obj.getClass().getName()
					+ "not appropriate class.\n Should extend JsonSerializable");
		}
	}

	public User getUser() {
		if (!clazz.isAssignableFrom(JsonSerializable.class)) { return null; }
		return (User) obj;
	}

}
