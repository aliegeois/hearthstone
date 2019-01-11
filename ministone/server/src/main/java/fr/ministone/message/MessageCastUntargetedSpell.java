package fr.ministone.message;

public class MessageCastUntargetedSpell extends GameMessage {

	private int source;

	public MessageCastUntargetedSpell() {}

	public MessageCastUntargetedSpell(boolean player1, int idCardSource) {
		super(player1);
		this.source = idCardSource;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int idCard) {
		this.source = idCard;
	}
}