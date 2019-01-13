package fr.ministone.message;

public class MessageCastTargetedSpell {
	private boolean own;
	private String cardId, targetId;

	public MessageCastTargetedSpell() {}

	public MessageCastTargetedSpell(boolean own, String cardId, String targetId) {
		this.own = own;
		this.cardId = cardId;
		this.targetId = targetId;
	}

	public boolean isOwn() {
		return own;
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

	public void setCardId(String newCardId) {
		cardId = newCardId;
	}

	public void setTargetId(String newTargetId) {
		targetId = newTargetId;
	}
}