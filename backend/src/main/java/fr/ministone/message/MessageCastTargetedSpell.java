package fr.ministone.message;

public class MessageCastTargetedSpell {
	private boolean own, hero;
	private String cardId, targetId;

	public MessageCastTargetedSpell() {}

	public MessageCastTargetedSpell(String own, String hero, String cardId, String targetId) {
		this.hero = "true".equals(hero);
		this.own = "true".equals(own);
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

	public void setOwn(String newOwn) {
		own = "true".equals(newOwn);
	}

	public void setHero(String newHero) {
		hero = "true".equals(newHero);
	}

	public void setCardId(String newCardId) {
		cardId = newCardId;
	}

	public void setTargetId(String newTargetId) {
		targetId = newTargetId;
	}
}