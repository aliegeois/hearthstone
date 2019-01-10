package main.java.game;

import java.util.HashMap;
import java.util.Map;
import main.java.game.Constants;

class Game implements GameEvent {
	private Player player1, player2;
	private Player playing, starting;
	private int turn;
	
	Game() {	
	}

	Game(Player player1, Player player2) {		
		this.player1 = player1;
		this.player2 = player2;
		
		player1.setOpponent(player2);
		
		this.turn = 0;
	}
	
	void addPlayer(String name, Player p) {
		if(player1 == null){
			player1 = p;
		}else if(player2 == null){
			player2 = p;
			player1.setOpponent(player2);
		}	
	}
	
	public void start() {
		double val = Math.random();
		if(val < 0.5){
			playing = this.player1;
		}else{
			playing = this.player2;
		}
		starting = playing;
	}
	
	public void playMinion(Player player, String cardId) {
		player.playMinion(cardId);
	}

	public void attackMinion(Player player, String minionId1, String minionId2) {
		CardMinion minion1 = player.getBoard().get(minionId1);
		CardMinion minion2 = player.getOpponent().getBoard().get(minionId2);
		player.attack(minion1, minion2);
	}

	public void useSpell(Player player, String cardId) {
		player.useSpell(cardId);
	}

	public void heroSpecial(Player player, Entity target) {
		player.heroSpecial(target);
	}


	@Override
	public void endTurn(Player player) {
		if(this.playing == player){
			if(playing == player1){
				playing = player2;
			}else{
				playing = player1;
			}
		}
		if(playing == starting){
			turn++;
		}	
	}
	
	public Player getPlaying() {
		return playing;
	}
	
	public int getTurn() {
		return turn;
	}
}
