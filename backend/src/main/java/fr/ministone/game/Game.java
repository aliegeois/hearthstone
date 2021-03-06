package fr.ministone.game;

import fr.ministone.JsonUtil;
import fr.ministone.User;
import fr.ministone.repository.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.core.AbstractMessageSendingTemplate;

public class Game implements IGame {
	protected String id;
	
	protected AbstractMessageSendingTemplate<String> template;
	protected CardMinionRepository cardMinionRepository;
	protected CardSpellRepository cardSpellRepository;

	protected Map<String, IPlayer> players = new HashMap<>();
	protected IPlayer playing;

	/**
	 * 
	 * @param id Identifiant de la Game
	 * @param template Objet permettant d'envoyer des messages
	 * @param user1 Joueur 1
	 * @param user2 Joueur 2
	 * @param cardMinionRepository Stockage des cartes serviteur
	 * @param cardSpellRepository Stockage des cartes sort
	 */
	public Game(String id, AbstractMessageSendingTemplate<String> template, User user1, User user2, CardMinionRepository cardMinionRepository, CardSpellRepository cardSpellRepository) {
		this.id = id;
		this.template = template;
		this.cardMinionRepository = cardMinionRepository;
		this.cardSpellRepository = cardSpellRepository;

		IPlayer player1 = new Player(user1.getName(), user1.getSessionId(), id, user1.getHeroType(), template, cardMinionRepository, cardSpellRepository);
		IPlayer player2 = new Player(user2.getName(), user2.getSessionId(), id, user2.getHeroType(), template, cardMinionRepository, cardSpellRepository);
		
		this.players.put(user1.getName(), player1);
		this.players.put(user2.getName(), player2);

		if(Math.random() > .5) {
			playing = player1;
			player1.setOpponent(player2);
		} else {
			playing = player2;
			player2.setOpponent(player1);
		}
	}

	@Override
	public void receiveConfirmStart(String playerName) {
		IPlayer p = players.get(playerName);
		p.readyToStart();
		if(p.getOpponent().isReady()) {
			start();
		}
	}

	@Override
	public void receiveSummonMinion(String playerName, String cardId) {
		IPlayer p = players.get(playerName);
		if(playerName.equals(playing.getName())) {
			p.summonMinion(cardId);
			checkBoard();
		}
	}

	@Override
	public void receiveAttack(String playerName, boolean isHero, String cardId, String targetId) {
		IPlayer p = players.get(playerName);
		if(playerName.equals(playing.getName())) {
			p.attack(isHero, cardId, targetId);
			checkBoard();
		}
	}

	@Override
	public void receiveCastSpell(String playerName, String cardId) {
		IPlayer p = players.get(playerName);
		if(playerName.equals(playing.getName())) {
			p.castSpell(cardId);
			checkBoard();
		}
	}

	@Override
	public void receiveCastSpell(String playerName, boolean own, boolean isHero, String cardId, String targetId) {
		IPlayer p = players.get(playerName);
		if(playerName.equals(playing.getName())) {
			p.castSpell(own, isHero, cardId, targetId);
			checkBoard();
		}
	}

	@Override
	public void receiveHeroSpecial(String playerName) {
		IPlayer p = players.get(playerName);
		if(playerName.equals(playing.getName())) {
			p.heroSpecial();
			checkBoard();
		}
	}

	@Override
	public void receiveHeroSpecial(String playerName, boolean own, boolean isHero, String targetId) {
		IPlayer p = players.get(playerName);
		if(playerName.equals(playing.getName())) {
			p.heroSpecial(own, isHero, targetId);
			checkBoard();
		}
	}

	@Override
	public void receiveEndTurn(String playerName) {
		if(playerName.equals(playing.getName())) {
			IPlayer opponent = playing.getOpponent();
			opponent.nextTurn();
			playing = opponent;
		}
	}


	@Override
    public void sendVictory(String playerName) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", playerName);
		template.convertAndSend("/topic/game/" + id + "/victory", JsonUtil.toJSON(send));
	}


	@Override
	public void start() {
		for(int i = 0; i < 4; i++) {
			playing.drawCard(true);
			playing.getOpponent().drawCard(true);
		}
		//playing.getOpponent().drawCard(true);
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
	public String getId() {
		return id;
	}

	@Override
	public boolean checkBoard() {
		for(IPlayer p : players.values()) {
			if(p.checkDead()) {
				return true; 
			}
		}
		return false;
	}
}
