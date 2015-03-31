package pl.grm.sconn.commands;

public enum Commands {
	NONE(
			"",
			null) ,
	SEND_ALL(
			"sendall",
			CommandType.SERVER) ,
	CLOSE(
			"close",
			CommandType.BOTH) ,
	CLOSECONN(
			"closeConn",
			CommandType.BOTH) ,
	CONNECTIONS(
			"connections",
			CommandType.SERVER) ,
	LIST(
			"list",
			CommandType.SERVER) ,
	STOP(
			"stop",
			CommandType.BOTH);
	
	private String		command;
	private CommandType	type;
	
	private Commands(String name, CommandType type) {
		this.command = name;
		this.type = type;
	}
	
	public static Commands getCommand(String name) {
		for (Commands commT : Commands.values()) {
			if (commT == NONE) {
				continue;
			}
			if (name.toLowerCase().contains(commT.getCommandString())) { return commT; }
		}
		return NONE;
	}
	
	public String getCommandString() {
		return "!" + command;
	}
	
	public CommandType getType() {
		return type;
	}
}


enum CommandType {
	SERVER(
			1) ,
	CLIENT(
			2) ,
	BOTH(
			3);
	
	private int	id;
	
	private CommandType(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}
