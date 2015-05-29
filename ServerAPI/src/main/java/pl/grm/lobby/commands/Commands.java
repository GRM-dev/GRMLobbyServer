package pl.grm.lobby.commands;

public enum Commands {
	NONE("", CommandType.NONE, false),
	ERROR("error", CommandType.NONE, false),
	SEND_ALL("sendall", CommandType.SERVER, true),
	CLOSE("close", CommandType.SERVER, false),
	CLOSECONN("closeConn", CommandType.BOTH, true),
	CONNECTIONS("connections", CommandType.SERVER, true),
	LIST("list", CommandType.BOTH, true),
	STOP("stop", CommandType.SERVER, true),
	START("start", CommandType.SERVER, false),
	JSON("json", CommandType.BOTH, true),
	MSG("msg", CommandType.SERVER, true),
	SAY("say", CommandType.CLIENT, true);

	private String command;
	private CommandType type;
	private boolean requireOnline;

	private Commands(String name, CommandType type, boolean requireOnline) {
		this.command = name;
		this.type = type;
		this.requireOnline = requireOnline;
	}

	public static Commands getCommand(String commS) {
		for (Commands commT : Commands.values()) {
			if (commT == NONE || commT == ERROR) {
				continue;
			}
			commS = commS.toLowerCase() + " ";
			String commandS = commT.getCommandString().toLowerCase() + " ";
			if (commS.startsWith(commandS)) { return commT; }
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

	public boolean hasToBeOnline() {
		return requireOnline;
	}
}
