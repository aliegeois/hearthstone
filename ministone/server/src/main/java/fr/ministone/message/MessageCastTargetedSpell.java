package fr.ministone.message;

public class MessageCastTargetedSpell {
	private boolean own, hero;
	private String cardId, targetId;

	public MessageCastTargetedSpell() {}

	public MessageCastTargetedSpell(boolean own, boolean hero, String cardId, String targetId) {
		this.hero = hero;
		this.own = own;
		this.cardId = cardId;
		this.targetId = targetId;
	}

	public boolean isOwn() {
		return own;
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

	public void setOwn(boolean newOwn) {
		own = newOwn;
	}

	public void setHero(boolean newHero) {
		hero = newHero;
	}

	public void setCardId(String newCardId) {
		cardId = newCardId;
	}

	public void setTargetId(String newTargetId) {
		targetId = newTargetId;
	}
}