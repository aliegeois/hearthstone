package game;

import java.util.HashMap;
import java.util.Map;

class Game implements GameEvent {
	private Map<String, Player> players = new HashMap<>();
	private String playing;
	
	private int turn;
	
	void addPlayer(String name, Player p) {
		players.put(name, p);
		if(players.size() == 2) {
			for(Player p1 : players.values()) {
				for(Player p2 : players.values()) {
					if(p1.getName() != p2.getName()) {
						p1.setOpponent(p2);
						p2.setOpponent(p1);
					}
				}
			}
			start();
		}
	}
	
	private void start() {
		String[] names = (String[])players.keySet().toArray();
		playing = names[(int)(Math.random() * names.length)];
	}
	
	public void playMinion(String playerName, int cardId) {
		players.get(playerName).playMinion(cardId);
	}

	public void attackMinion(String playerName, int minionId1, int minionId2) {
		Player player = players.get(playerName);
		CardMinion minion1 = player.getBoard().get(minionId1);
		CardMinion minion2 = player.getOpponent().getBoard().get(minionId2);
		players.get(playerName).attack(minion1, minion2);
	}

	public void useSpell(String playerName, int cardId) {
		players.get(playerName).useSpell(cardId);
	}

	public void heroSpecial(String playerName, Entity target) {
		players.get(playerName).heroSpecial(target);
	}

	public void endTurn(String playerName) {
		//this.players.get(playerName).endTurn();
		turn++;
	}
	
	public String getPlaying() {
		return playing;
	}
	
	public int getTurn() {
		return turn;
	}
}
