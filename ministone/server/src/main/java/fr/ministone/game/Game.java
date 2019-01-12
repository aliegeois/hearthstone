package fr.ministone.game;

import fr.ministone.JSONeur;
import fr.ministone.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.messaging.simp.SimpMessagingTemplate;

public class Game implements IGameMessageReceiver, IGameMessageSender {
	private SimpMessagingTemplate template;
	private Map<String, Player> players = new HashMap<>();
	private Player playing;
	private int turn;
	private UUID id;
	
	public Game(UUID id, SimpMessagingTemplate template, User user1, User user2) {
		this.id = id;
		this.template = template;
		Player player1 = new Player(user1.getName(), user1.getSessionId());
		Player player2 = new Player(user2.getName(), user2.getSessionId());
		player1.setOpponent(player2);
		this.players.put(user1.getName(), player1);
		this.players.put(user2.getName(), player2);
		this.turn = 0;
	}
	
	public void start(Player p1, Player p2) {
		double val = Math.random();
		playing = val > .5 ? p1 : p2;
		for(int i = 0; i < 3; i++) {
			playing.drawCard();
			playing.getOpponent().drawCard();
		}
		playing.getOpponent().drawCard();
		playing.nextTurn();
		sendNextTurn(playing.getName());
		sendIsStarting(playing.getName());
	}

	public void receiveSetHero(String playerName, String heroType) {
		Player p = players.get(playerName);
		if(playing.getName() == playerName) {
			p.setHero(heroType);
			sendSummonMinion(playerName, heroType);
		}
	}

	public void receiveSummonMinion(String playerName, String cardId) {
		Player p = players.get(playerName);
		if(playing.getName() == playerName) {
			p.summonMinion(cardId);
			sendSummonMinion(playerName, cardId);
		}
	}

	public void receiveAttack(String playerName, String cardId, String targetId) {
		Player p = players.get(playerName);
		if(playing.getName() == playerName) {
			p.attack(cardId, targetId);
			sendAttack(playerName, cardId, targetId);
		}
	}

	public void receiveCastSpell(String playerName, String cardId) {
		Player p = players.get(playerName);
		p.castSpell(cardId);
		sendCastUntargetedSpell(playerName, cardId);
	}

	public void receiveCastSpell(String playerName, boolean own, String cardId, String targetId) {
		Player p = players.get(playerName);
		if(playing.getName() == playerName) {
			p.castSpell(own, cardId, targetId);
			sendCastTargetedSpell(playerName, own, cardId, targetId);
		}
	}

	public void receiveSpecial(String playerName) {
		Player p = players.get(playerName);
		if(playing.getName() == playerName) {
			p.heroSpecial();
			sendUntargetedSpecial(playerName);
		}
	}

	public void receiveSpecial(String playerName, boolean own, String targetId) {
		Player p = players.get(playerName);
		if(playing.getName() == playerName) {
			p.heroSpecial(own, targetId);
			sendTargetedSpecial(playerName, own, targetId);
		}
	}

	public void receiveEndTurn(String playerName) { // À faire
		if(playing.getName() == playerName) {
			endTurn();
		}
	}


	public void sendIsStarting(String playerName) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		// Vérifier que les reçeveurs côté client on la même URL
		template.convertAndSend("/topic/game/" + id + "/isStarting", JSONeur.toJSON(send));
	}

    public void sendSummonMinion(String playerName, String cardId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		send.put("cardId", cardId);
		template.convertAndSend("/topic/game/" + id + "/summonMinion", JSONeur.toJSON(send));
	}

    public void sendAttack(String playerName, String cardId, String targetId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		send.put("cardId", cardId);
		send.put("targetId", targetId);
		template.convertAndSend("/topic/game/" + id + "/attack", JSONeur.toJSON(send));
	}

	public void sendCastTargetedSpell(String playerName, boolean own, String cardId, String targetId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		send.put("cardId", cardId);
		send.put("targetId", targetId);
		template.convertAndSend("/topic/game/" + id + "/castTargetedSpell", JSONeur.toJSON(send));
	}

    public void sendCastUntargetedSpell(String playerName, String cardId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		send.put("cardId", cardId);
		template.convertAndSend("/topic/game/" + id + "/castUntargetedSpell", JSONeur.toJSON(send));
	}

	public void sendTargetedSpecial(String playerName, boolean own, String targetId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		send.put("own", own ? "true" : "false");
		send.put("targetId", targetId);
		template.convertAndSend("/topic/game/" + id + "/targetedSpecial", JSONeur.toJSON(send));
	}

    public void sendUntargetedSpecial(String playerName) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		template.convertAndSend("/topic/game/" + id + "/untargetedSpecial", JSONeur.toJSON(send));
	}

	public void sendEndTurn(String playerName) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		template.convertAndSend("/topic/game/" + id + "/endTurn", JSONeur.toJSON(send));
	}

	public void sendNextTurn(String playerName) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		template.convertAndSend("/topic/game/" + id + "/nextTurn", JSONeur.toJSON(send));
	}

    public void sendTimeout(String playerName) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		template.convertAndSend("/topic/game/" + id + "/timeout", JSONeur.toJSON(send));
	}

    public void sendDrawCard(String playerName, String cardName) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		send.put("cardName", cardName);
		template.convertAndSend("/topic/game/" + id + "/drawCard", JSONeur.toJSON(send));
	}

    public void sendOpponentDrawCard(String playerName, String cardName) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		send.put("cardName", cardName);
		template.convertAndSend("/topic/game/" + id + "/opponentDrawCard", JSONeur.toJSON(send));
	}

    public void sendWin(String playerName) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		template.convertAndSend("/topic/game/" + id + "/win", JSONeur.toJSON(send));
	}

    public void sendLose(String playerName) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		template.convertAndSend("/topic/game/" + id + "/lose", JSONeur.toJSON(send));
	}


	private void endTurn() {
		Player opponent = playing.getOpponent();
		sendEndTurn(playing.getName());
		opponent.nextTurn();
		sendNextTurn(opponent.getName());
		playing = opponent;
	}

	
	public Player getPlaying() {
		return playing;
	}
	
	public int getTurn() {
		return turn;
	}

	public void checkBoard() {
		for(Player p : players.values()) {
			p.checkDead();
		}
	}
}
