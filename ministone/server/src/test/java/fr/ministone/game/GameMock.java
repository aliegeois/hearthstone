package fr.ministone.game;

import fr.ministone.User;
import fr.ministone.game.IGame;
import fr.ministone.game.IPlayer;

import java.util.HashMap;
import java.util.Map;

public class GameMock implements IGame, IGameMessageReceiver, IGameMessageSender {
	private Map<String, IPlayer> players = new HashMap<>();
	private IPlayer playing;
	//private int turn;
	
	public GameMock(User user1, User user2) {
		IPlayer player1 = new MPlayer("warrior");
		IPlayer player2 = new MPlayer("paladin");
		player1.setOpponent(player2);
		this.players.put(user1.getName(), player1);
		this.players.put(user2.getName(), player2);
		//this.turn = 0;
	}
    
    @Override
	public void start() {
		IPlayer p1 = null, p2 = null;

		for(IPlayer p : players.values()) { 
			if(p1 == null)
				p1 = p;
			else
				p2 = p;
		}

		playing = p1;
		for(int i = 0; i < 3; i++) {
			playing.drawCard(false);
			playing.getOpponent().drawCard(false);
		}
		playing.getOpponent().drawCard(false);
		playing.nextTurn();
		//sendNextTurn(playing.getName());
		//sendIsStarting(playing.getName());
	}

	@Override
	public void receiveSummonMinion(String playerName, String cardId) {
		
	}

	//@Override
	public void receiveAttack(String playerName, String cardId, String targetId) {
		
	}

	@Override
	public void receiveCastSpell(String playerName, String cardId) {
		
	}

	//@Override
	public void receiveCastSpell(String playerName, boolean own, String cardId, String targetId) {
		
	}

	@Override
	public void receiveHeroSpecial(String playerName) {
	
	}

	//@Override
	public void receiveHeroSpecial(String playerName, boolean own, String targetId) {
	
	}

	@Override
	public void receiveEndTurn(String playerName) { // À faire
		
	}
	
	/*@Override
	public void sendSetHero(String playerName, String heroType) {
	
	}

	@Override
    public void sendSetOpponentHero(String playerName, String heroType) {
		
	}

	@Override
	public void sendIsStarting(String playerName) {
		
	}*/

	@Override
    public void sendVictory(String playerName) {
		
	}


    @Override
	public boolean containsPlayer(String sessionId) {
		return getPlayer(sessionId) != null;
	}

    @Override
	public IPlayer getPlayer(String sessionId) {
		for(IPlayer p : players.values())
			if(sessionId.equals(p.getSessionId()))
				return p;
		return null;
	}
    
    @Override
	public IPlayer getPlaying() {
		return playing;
	}
    
    @Override
	public void checkDead() {
		for(IPlayer p : players.values()) {
			p.checkDead();
		}
	}

	@Override
	public void receiveAttack(String playerName, boolean isHero, String cardId, String targetId) {

	}

	@Override
	public void receiveCastSpell(String playerName, boolean own, boolean isHero, String cardId, String targetId) {

	}

	@Override
	public void receiveHeroSpecial(String playerName, boolean own, boolean isHero, String targetId) {

	}
}
