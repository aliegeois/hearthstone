package fr.ministone.message;

public class MessageHeroTargetedSpecial {
	private boolean own, hero;
	private String targetId;

	public MessageHeroTargetedSpecial() {}

	public MessageHeroTargetedSpecial(String own, String hero, String targetId) {
		this.own = "true".equals(own);
		this.hero = "true".equals(hero);
		this.targetId = targetId;
	}

	public boolean isOwn() {
		return own;
	}

	public boolean isHero() {
		return hero;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setOwn(boolean newOwn) {
		own = newOwn;
	}

	public void setHero(boolean newHero) {
		hero = newHero;
	}

	public void setTargetId(String newTargetId) {
		targetId = newTargetId;
	}
}