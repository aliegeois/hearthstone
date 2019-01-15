package fr.ministone.message;

public class MessageAttack {
	boolean hero;
	private String cardId, targetId;

	public MessageAttack() {}

	public MessageAttack(boolean hero, String cardId, String targetId) {
		this.hero = hero;
		this.cardId = cardId;
		this.targetId = targetId;
	}

	public boolean isHero() {
		return hero;
	}

	public String getCardId() {
		return cardId;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setHero(boolean newHero) {
		hero = newHero;
	}

	public void setSource(String newCardId) {
		cardId = newCardId;
	}

	public void setTarget(String newTargetId) {
		targetId = newTargetId;
	}
}