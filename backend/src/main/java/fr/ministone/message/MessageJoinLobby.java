package fr.ministone.message;

public class MessageJoinLobby {
	private String name;
	private String level;
	private String heroType;

	public MessageJoinLobby() {}

	public MessageJoinLobby(String name, String level, String heroType) {
		this.name = name;
		this.level = level;
		this.heroType = heroType;
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

	public String getHeroType() {
		return heroType;
	}

	public void setHeroType(String newHeroType) {
		heroType = newHeroType;
	}
}