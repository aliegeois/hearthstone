package fr.ministone.game;

import fr.ministone.User;

import java.util.HashMap;
import java.util.Map;

public class GameMock implements IGame{
	private Map<String, IPlayer> players = new HashMap<>();
	private IPlayer playing;
	private int turn;
	
	public GameMock(User user1, User user2){
		IPlayer player1 = new PlayerMock(user1.getName(), "E");
		IPlayer player2 = new PlayerMock(user2.getName(), "F");
		player1.setOpponent(player2);
		player1.setHero("paladin");
		player2.setHero("mage");
		this.players.put(user1.getName(), player1);
		this.players.put(user2.getName(), player2);
		this.turn = 0;
	}
    
    @Override
	public void start(IPlayer p1, IPlayer p2) {
		playing = p1;
		for(int i = 0; i < 3; i++) {
			playing.drawCard();
			playing.getOpponent().drawCard();
		}
		playing.getOpponent().drawCard();
		playing.nextTurn();
		sendNextTurn(playing.getName());
		sendIsStarting(playing.getName());
	}

	@Override
	public void receiveSetHero(String playerName, String heroType) {
		IPlayer p = players.get(playerName);
		if(playing.getName() == playerName) {
			p.setHero(heroType);
			sendSummonMinion(playerName, heroType);
		}
	}

	@Override
	public void receiveSummonMinion(String playerName, String cardId) {
		IPlayer p = players.get(playerName);
		if(playing.getName() == playerName) {
			p.summonMinion(cardId);
			sendSummonMinion(playerName, cardId);
		}
	}

	@Override
	public void receiveAttack(String playerName, String cardId, String targetId) {
		IPlayer p = players.get(playerName);
		if(playing.getName() == playerName) {
			p.attack(cardId, targetId);
			sendAttack(playerName, cardId, targetId);
		}
	}

	@Override
	public void receiveCastSpell(String playerName, String cardId) {
		IPlayer p = players.get(playerName);
		p.castSpell(cardId);
		sendCastUntargetedSpell(playerName, cardId);
	}

	@Override
	public void receiveCastSpell(String playerName, boolean own, String cardId, String targetId) {
		IPlayer p = players.get(playerName);
		if(playing.getName() == playerName) {
			p.castSpell(own, cardId, targetId);
			sendCastTargetedSpell(playerName, own, cardId, targetId);
		}
	}

	@Override
	public void receiveHeroSpecial(String playerName) {
		IPlayer p = players.get(playerName);
		if(playing.getName() == playerName) {
			p.heroSpecial();
			sendHeroUntargetedSpecial(playerName);
		}
	}

	@Override
	public void receiveHeroSpecial(String playerName, boolean own, String targetId) {
		IPlayer p = players.get(playerName);
		if(playing.getName() == playerName) {
			p.heroSpecial(own, targetId);
			sendHeroTargetedSpecial(playerName, own, targetId);
		}
	}

	@Override
	public void receiveEndTurn(String playerName) { // À faire
		if(playing.getName() == playerName) {
			endTurn();
		}
	}
	
	@Override
	public void sendSetHero(String playerName, String heroType) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		send.put("heroType", heroType);
	}

	@Override
    public void sendSetOpponentHero(String playerName, String heroType) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		send.put("heroType", heroType);
	}

	@Override
	public void sendIsStarting(String playerName) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
	}

	@Override
    public void sendSummonMinion(String playerName, String cardId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		send.put("cardId", cardId);
	}

	@Override
    public void sendAttack(String playerName, String cardId, String targetId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		send.put("cardId", cardId);
		send.put("targetId", targetId);
	}

	@Override
	public void sendCastTargetedSpell(String playerName, boolean own, String cardId, String targetId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		send.put("cardId", cardId);
		send.put("targetId", targetId);
	}

	@Override
    public void sendCastUntargetedSpell(String playerName, String cardId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		send.put("cardId", cardId);
	}

	@Override
	public void sendHeroTargetedSpecial(String playerName, boolean own, String targetId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		send.put("own", own ? "true" : "false");
		send.put("targetId", targetId);
	}

	@Override
    public void sendHeroUntargetedSpecial(String playerName) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
	}

	@Override
	public void sendEndTurn(String playerName) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
	}

	@Override
	public void sendNextTurn(String playerName) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
	}

	@Override
    public void sendTimeout(String playerName) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
	}

	@Override
    public void sendDrawCard(String playerName, String cardName) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		send.put("cardName", cardName);
	}

	@Override
    public void sendOpponentDrawCard(String playerName, String cardName) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		send.put("cardName", cardName);
	}

	@Override
    public void sendWin(String playerName) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
	}

	@Override
    public void sendLose(String playerName) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
	}

    @Override
	public void endTurn() {
		IPlayer opponent = playing.getOpponent();
		sendEndTurn(playing.getName());
		opponent.nextTurn();
		sendNextTurn(opponent.getName());
		playing = opponent;
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
	public int getTurn() {
		return turn;
	}

    @Override
	public void checkBoard() {
		for(IPlayer p : players.values()) {
			p.checkDead();
		}
	}
}
