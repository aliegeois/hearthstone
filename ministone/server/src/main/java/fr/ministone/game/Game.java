package fr.ministone.game;

import fr.ministone.JSONeur;
import fr.ministone.User;
import fr.ministone.repository.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.core.AbstractMessageSendingTemplate;

public class Game implements IGame {
	protected String id;
	
	protected AbstractMessageSendingTemplate<String> template;

	protected Map<String, IPlayer> players = new HashMap<>();
	protected IPlayer playing;

	public Game(String id, AbstractMessageSendingTemplate<String> template, User user1, User user2, CardMinionRepository cardMinionRepository, CardSpellRepository cardSpellRepository) {
		this.id = id;
		this.template = template;
		System.out.println("Nouvelle game (1)");
		IPlayer player1 = new Player(user1.getName(), user1.getSessionId(), id, user1.getHeroType(), template, cardMinionRepository, cardSpellRepository);
		IPlayer player2 = new Player(user2.getName(), user2.getSessionId(), id, user2.getHeroType(), template, cardMinionRepository, cardSpellRepository);
		player1.setOpponent(player2);
		this.players.put(user1.getName(), player1);
		this.players.put(user2.getName(), player2);
		start();
	}

	public Game(String id, AbstractMessageSendingTemplate<String> template, User user1, User user2) {
		this.id = id;
		this.template = template;
		System.out.println("Nouvelle game (2)");
		IPlayer player1 = new Player(user1.getName(), user1.getSessionId(), id, user1.getHeroType());
		IPlayer player2 = new Player(user2.getName(), user2.getSessionId(), id, user2.getHeroType());
		player1.setOpponent(player2);
		this.players.put(user1.getName(), player1);
		this.players.put(user2.getName(), player2);
		start();
	}


	@Override
	public void receiveSummonMinion(String playerName, String cardId) {
		IPlayer p = players.get(playerName);
		if(playerName.equals(playing.getName())) {
			p.summonMinion(Long.parseLong(cardId));
			//sendSummonMinion(playerName, cardId);
		}
	}

	@Override
	public void receiveAttack(String playerName, boolean isHero, String cardId, String targetId) {
		IPlayer p = players.get(playerName);
		if(playerName.equals(playing.getName())) {
			p.attack(isHero, Long.parseLong(cardId), Long.parseLong(targetId));
			//sendAttack(playerName, cardId, targetId);
		}
	}

	@Override
	public void receiveCastSpell(String playerName, String cardId) {
		IPlayer p = players.get(playerName);
		if(playerName.equals(playing.getName())) {
			p.castSpell(Long.parseLong(cardId));
			//sendCastUntargetedSpell(playerName, cardId);
		}
	}

	@Override
	public void receiveCastSpell(String playerName, boolean own, boolean isHero, String cardId, String targetId) {
		IPlayer p = players.get(playerName);
		if(playerName.equals(playing.getName())) {
			p.castSpell(own, isHero, Long.parseLong(cardId), Long.parseLong(targetId));
			//sendCastTargetedSpell(playerName, own, cardId, targetId);
		}
	}

	@Override
	public void receiveHeroSpecial(String playerName) {
		IPlayer p = players.get(playerName);
		if(playerName.equals(playing.getName())) {
			p.heroSpecial();
			//sendHeroUntargetedSpecial(playerName);
		}
	}

	@Override
	public void receiveHeroSpecial(String playerName, boolean own, boolean isHero, String targetId) {
		IPlayer p = players.get(playerName);
		if(playerName.equals(playing.getName())) {
			p.heroSpecial(own, isHero, Long.parseLong(targetId));
			//sendHeroTargetedSpecial(playerName, own, targetId);
		}
	}

	@Override
	public void receiveEndTurn(String playerName) {
		if(playerName.equals(playing.getName())) {
			IPlayer opponent = playing.getOpponent();
			//sendEndTurn(playing.getName());
			opponent.nextTurn();
			//sendNextTurn(opponent.getName());
			playing = opponent;
		}
	}


	/*// TODO : receive coté client
	@Override
	public void sendIsStarting(String playerName) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		// Vérifier que les reçeveurs côté client on la même URL
		template.convertAndSend("/topic/game/" + id + "/isStarting", JSONeur.toJSON(send));
	}*/

	/*
	@Override
    public void sendSummonMinion(String playerName, String cardId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		send.put("cardId", cardId);
		template.convertAndSend("/topic/game/" + id + "/summonMinion", JSONeur.toJSON(send));
	}

	@Override
    public void sendAttack(String playerName, String cardId, String targetId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		send.put("cardId", cardId);
		send.put("targetId", targetId);
		template.convertAndSend("/topic/game/" + id + "/attack", JSONeur.toJSON(send));
	}

	@Override
	public void sendCastTargetedSpell(String playerName, boolean own, String cardId, String targetId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		send.put("cardId", cardId);
		send.put("targetId", targetId);
		send.put("own", own ? "true" : "false");
		template.convertAndSend("/topic/game/" + id + "/castTargetedSpell", JSONeur.toJSON(send));
	}

	@Override
    public void sendCastUntargetedSpell(String playerName, String cardId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		send.put("cardId", cardId);
		template.convertAndSend("/topic/game/" + id + "/castUntargetedSpell", JSONeur.toJSON(send));
	}

	@Override
	public void sendHeroTargetedSpecial(String playerName, boolean own, String targetId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		send.put("own", own ? "true" : "false");
		send.put("targetId", targetId);
		template.convertAndSend("/topic/game/" + id + "/targetedSpecial", JSONeur.toJSON(send));
	}

	@Override
    public void sendHeroUntargetedSpecial(String playerName) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		template.convertAndSend("/topic/game/" + id + "/untargetedSpecial", JSONeur.toJSON(send));
	}

	@Override
	public void sendNextTurn(String playerName) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		template.convertAndSend("/topic/game/" + id + "/nextTurn", JSONeur.toJSON(send));
	}

	@Override
    public void sendTimeout(String playerName) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		template.convertAndSend("/topic/game/" + id + "/timeout", JSONeur.toJSON(send));
	}

	@Override
    public void sendDrawCard(String playerName, String cardName, String cardId, String cardType) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		send.put("cardName", cardName);
		send.put("cardId", cardId);
		send.put("cardType", cardType);
		template.convertAndSend("/topic/game/" + id + "/drawCard", JSONeur.toJSON(send));
	}*/

	@Override
    public void sendVictory(String playerName) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		template.convertAndSend("/topic/game/" + id + "/victory", JSONeur.toJSON(send));
	}


	@Override
	public void start() {
		IPlayer p1 = null, p2 = null;

		for(IPlayer p : players.values()) { // Je sais, c'est sale
			if(p1 == null)
				p1 = p;
			else
				p2 = p;
		}

		playing = Math.random() > .5 ? p1 : p2;
		for(int i = 0; i < 3; i++) {
			playing.drawCard(true);
			playing.getOpponent().drawCard(true);
		}
		playing.getOpponent().drawCard(true);
		playing.nextTurn();
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
}
