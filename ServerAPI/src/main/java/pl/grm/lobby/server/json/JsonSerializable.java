package pl.grm.lobby.server.json;

import java.io.Serializable;

import org.json.JSONObject;

public abstract class JsonSerializable implements Serializable {

	private static final long serialVersionUID = 1L;

	public abstract JSONObject getAsJsonObject();

	@Override
	public String toString() {
		JSONObject obj = getAsJsonObject();
		return obj.toString();
	}
}
