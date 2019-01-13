package fr.ministone.message;

public class MessageAttack {
	private String cardId, targetId;

	public MessageAttack() {}

	public MessageAttack(String cardId, String targetId) {
		this.cardId = cardId;
		this.targetId = targetId;
	}

	public String getCardId() {
		return cardId;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setSource(String newCardId) {
		cardId = newCardId;
	}

	public void setTarget(String newTargetId) {
		targetId = newTargetId;
	}
}