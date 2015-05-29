package pl.grm.lobby.data;

import org.json.JSONObject;

import pl.grm.lobby.json.JsonSerializable;

public class User extends JsonSerializable {

	private static final long serialVersionUID = 1L;
	private int ID;
	private String name;
	private int age;
	private String mail;

	public User(int ID, String name, int age, String mail) {
		this.ID = ID;
		this.name = name;
		this.age = age;
		this.mail = mail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	@Override
	public JSONObject getAsJsonObject() {
		JSONObject obj = new JSONObject();
		obj.put("ID", ID);
		obj.put("name", name);
		obj.put("age", age);
		obj.put("mail", mail);
		return obj;
	}
}
