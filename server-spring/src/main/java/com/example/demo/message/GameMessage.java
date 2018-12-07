package com.example.demo.message;

public abstract class GameMessage {
	protected boolean player1;
	
	public GameMessage() {}

	public GameMessage(boolean player1) {
		this.player1 = player1;
	}

	public boolean getPlayer1() {
		return player1;
	}

	public void setPlayer1(boolean value) {
		player1 = value;
	}
}