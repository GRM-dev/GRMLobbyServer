package pl.grm.lobby.commands;

public enum CommandType {
	NONE(0),
	SERVER(1),
	CLIENT(2),
	BOTH(3);

	private int id;

	private CommandType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}