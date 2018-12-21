package fr.ministone.server.message;

public class MessageJoinLobby {
	private String name;

	public MessageJoinLobby() {}

	public MessageJoinLobby(String name) {
		// faire des trucs
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String newName) {
		name = newName;
	}
}
