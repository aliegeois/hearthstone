package fr.ministone.message;

public class MessageSetHero {
	private String heroType;

	public MessageSetHero() {}

	public MessageSetHero(String heroType) {
		this.heroType = heroType;
	}

	public String getHeroType() {
		return heroType;
	}

	public void setHeroType(String newHeroType) {
		heroType = newHeroType;
	}
}