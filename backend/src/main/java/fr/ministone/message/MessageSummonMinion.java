package fr.ministone.message;

public class MessageSummonMinion {
	private String cardId;

	public MessageSummonMinion() {}

	public MessageSummonMinion(String cardId) {
		this.cardId = cardId;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String newCardId) {
		cardId = newCardId;
	}
}