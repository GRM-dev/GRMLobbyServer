package pl.grm.sconn.json;

import pl.grm.sconn.data.User;

/** Deserializabled and converted object */
public class DCObject {

	private Object obj;
	private Class<? extends JsonSerializable> clazz;
	private int action;

	@SuppressWarnings("unchecked")
	DCObject(Object obj, int action) {
		this.obj = obj;
		this.action = action;
		if (obj.getClass().isAssignableFrom(JsonSerializable.class)
				|| obj.getClass().getSuperclass() == JsonSerializable.class) {
			this.clazz = (Class<? extends JsonSerializable>) obj.getClass();
		} else {
			throw new ClassCastException(obj.getClass().getName()
					+ "not appropriate class.\n Should extend JsonSerializable");
		}
	}

	public User getUser() {
		if (clazz == User.class) { return (User) obj; }
		return null;
	}

	public int getAction() {
		return this.action;
	}
}
