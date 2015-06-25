package pl.grm.lobby.server.json;

public class JsonConvertException extends Exception {

	private static final long serialVersionUID = 1L;

	public JsonConvertException(String msg) {
		super(msg);
	}

	public JsonConvertException(String msg, Throwable t) {
		super(msg, t);
	}

	public JsonConvertException(Throwable t) {
		super(t);
	}
}
