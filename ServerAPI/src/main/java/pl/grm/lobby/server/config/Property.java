package pl.grm.lobby.server.config;

/**
 * Property to use with Config and load properties from file to program
 */
public enum Property {
						DATABASE_HOSTNAME("DB_Host", PropertyType.STRING),
						DATABASE_PORT("DB_", PropertyType.INT, "3306"),
						DATABASE_USERNAME("DB_Username", PropertyType.STRING),
						DATABASE_PASSWORD("DB_Password", PropertyType.STRING);

	private String propName;
	private PropertyType type;
	private String defaultValue;
	private boolean _hasDefault;

	Property(String name, PropertyType type, String... defaultValue) {
		this.propName = name;
		this.type = type;
		if (defaultValue != null) {
			this.defaultValue = defaultValue[0];
			this._hasDefault = true;
		} else {
			this._hasDefault = false;
		}
	}

	public String getName() {
		return this.propName;
	}

	public PropertyType getType() {
		return this.type;
	}

	public boolean hasDefault() {
		return this._hasDefault;
	}

	public String getDefault() {
		return this.defaultValue;
	}

	public enum PropertyType {
								STRING,
								INT,
								FLOAT;
	}
}
