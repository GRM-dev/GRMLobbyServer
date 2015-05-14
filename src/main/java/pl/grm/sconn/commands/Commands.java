package pl.grm.sconn.commands;

public enum Commands {
	NONE("", CommandType.NONE),
	ERROR("error", CommandType.NONE),
	SEND_ALL("sendall", CommandType.SERVER),
	CLOSE("close", CommandType.BOTH),
	CLOSECONN("closeConn", CommandType.BOTH),
	CONNECTIONS("connections", CommandType.SERVER),
	LIST("list", CommandType.SERVER),
	STOP("stop", CommandType.SERVER),
	START("start", CommandType.SERVER),
	JSON("json", CommandType.BOTH),
	MSG("msg", CommandType.BOTH);

	private String command;
	private CommandType type;

	private Commands(String name, CommandType type) {
		this.command = name;
		this.type = type;
	}

	public static Commands getCommand(String commS) {
		for (Commands commT : Commands.values()) {
			if (commT == NONE || commT == ERROR) {
				continue;
			}
			if (commS.toLowerCase().startsWith(commT.getCommandString())) { return commT; }
		}
		return NONE;
	}

	public static String getOffset(String commS) {
		Commands comm = getCommand(commS);
		if (comm == NONE) { return ""; }
		return commS.replace(comm.getCommandString(), "");
	}

	public String getCommandString() {
		return "!" + command;
	}

	public CommandType getType() {
		return type;
	}
}
