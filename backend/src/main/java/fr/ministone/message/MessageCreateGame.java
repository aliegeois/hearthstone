package fr.ministone.message;

public class MessageCreateGame {
	private String opponent;

	public MessageCreateGame() {}

	public MessageCreateGame(String opponent) {
		this.opponent = opponent;
	}

	public String getOpponent() {
		return opponent;
	}

	public void setOpponent(String newOpponent) {
		opponent = newOpponent;
	}
}