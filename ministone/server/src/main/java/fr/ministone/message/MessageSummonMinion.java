package fr.ministone.message;

public class MessageSummonMinion extends GameMessage {

	private int idCard;

	public MessageSummonMinion() {}

	public MessageSummonMinion(boolean player1, int idCard) {
		super(player1);
		this.idCard = idCard;
	}

	public int getIdCard() {
		return idCard;
	}

	public void setIdCard(int newIdCard) {
		idCard = newIdCard;
	}
}