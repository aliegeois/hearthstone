package fr.ministone.message;

public class MessageJoinLobby {
	private String name;
	private String level;

	public MessageJoinLobby() {}

	public MessageJoinLobby(String name, String level) {
		// faire des trucs
		this.name = name;
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String newName) {
		name = newName;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String newLevel) {
		level = newLevel;
	}
}