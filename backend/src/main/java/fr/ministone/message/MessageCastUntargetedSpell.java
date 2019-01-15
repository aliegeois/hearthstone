package fr.ministone.message;

public class MessageCastUntargetedSpell {
	private String cardId;

	public MessageCastUntargetedSpell() {}

	public MessageCastUntargetedSpell(String cardId) {
		this.cardId = cardId;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String newCardId) {
		cardId = newCardId;
	}
}