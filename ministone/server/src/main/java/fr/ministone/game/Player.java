package fr.ministone.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import fr.ministone.repository.*;

import fr.ministone.game.hero.*;
import fr.ministone.JSONeur;
import fr.ministone.game.card.*;

public class Player implements IPlayer {
	private String name;
	private String sessionId;
	private String gameId;

	private SimpMessagingTemplate template;

	private Set<Card> deck = new HashSet<>();
	private Map<String, Card> hand = new HashMap<>();
	private Map<String, CardMinion> board = new HashMap<>();
	
	private int manaMax = 0;
	private int mana = 0;
	
	private Hero hero;
	private IPlayer opponent;
	
	public Player(String name, String sessionId, String gameId, String heroType, CardMinionRepository cardMinionRepository, CardSpellRepository cardSpellRepository) {
		this.name = name;
		this.sessionId = sessionId;
		this.gameId = gameId;
		
		for(Iterator<CardMinion> i = cardMinionRepository.findAllByDeck("shared").iterator(); i.hasNext();)
			this.deck.add(i.next());
		for(Iterator<CardSpell> i = cardSpellRepository.findAllByDeck("shared").iterator(); i.hasNext();)
			this.deck.add(i.next());

		if("warrior".equals(heroType)) {
			this.hero = new HeroWarrior();
			for(Iterator<CardMinion> i = cardMinionRepository.findAllByDeck("warrior").iterator(); i.hasNext();)
				this.deck.add(i.next());
			for(Iterator<CardSpell> i = cardSpellRepository.findAllByDeck("warrior").iterator(); i.hasNext();)
				this.deck.add(i.next());
		} else if("paladin".equals(heroType)) {
			this.hero = new HeroPaladin();
			for(Iterator<CardMinion> i = cardMinionRepository.findAllByDeck("paladin").iterator(); i.hasNext();)
				this.deck.add(i.next());
			for(Iterator<CardSpell> i = cardSpellRepository.findAllByDeck("paladin").iterator(); i.hasNext();)
				this.deck.add(i.next());
		} else if("mage".equals(heroType)) {
			this.hero = new HeroMage();
			for(Iterator<CardMinion> i = cardMinionRepository.findAllByDeck("mage").iterator(); i.hasNext();)
				this.deck.add(i.next());
			for(Iterator<CardSpell> i = cardSpellRepository.findAllByDeck("mage").iterator(); i.hasNext();)
				this.deck.add(i.next());
		}
		this.hero.setPlayer(this);
	}

	public Player(String name, String sessionId, String heroType) {
		this.name = name;
		this.sessionId = sessionId;
		
		if("warrior".equals(heroType)) {
			this.hero = new HeroWarrior();
		} else if("paladin".equals(heroType)) {
			this.hero = new HeroPaladin();
		} else if("mage".equals(heroType)) {
			this.hero = new HeroMage();
		}
		this.hero.setPlayer(this);
	}
	
	@Override
	public void setOpponent(IPlayer p) {
		opponent = p;
		if(p.getOpponent() == null) {
			p.setOpponent(this);
		}
	}
	
	@Override
	public void summonMinion(String minionId) {
		CardMinion minion = (CardMinion)hand.get(minionId);
		int manaCost = minion.getManaCost();
		if(mana >= manaCost) {
			minion.play();
			mana -= manaCost;
			board.put(minionId, minion);
			hand.remove(minionId);
			sendSummonMinion(minionId);;
		} else {
			// Si on a le temps: faire un message de type "notEnoughMana" et l'envoyer
		}
	}
	
	@Override
	public void attack(String cardId, String targetId) { // Plus de vérifications (genre opponent card existe ou pas) ??
		CardMinion minion = (CardMinion)hand.get(cardId);
		if(targetId.equals("hero")) {
			minion.attack(opponent.getHero());
		} else {
			minion.attack(opponent.getBoard().get(targetId));
		}
		checkDead();
		sendAttack(cardId, targetId); // J'ai un doute sur l'ordre mdr
	}
	
	@Override
	public Card drawCard(boolean send) {
		Card cardDrawn = (Card)deck.toArray()[(int)(Math.random() * deck.size())];
		String cId = UUID.randomUUID().toString();

		Card carte = cardDrawn.copy();
		carte.setId(cId);
		hand.put(cId, carte);
		if(send)
			sendDrawCard(carte.getName(), cId, carte instanceof CardMinion ? "minion" : "spell");

		return cardDrawn;
	}

	@Override
	public void castSpell(boolean own, String spellId, String targetId) { // À terminer
		CardSpell spell = (CardSpell)hand.get(spellId);
		IEntity victim;
		if("hero".equals(targetId)) {
			victim = (own ? this : opponent).getHero();
		} else {
			victim = (own ? this : opponent).getBoard().get(targetId);
		}
		spell.play(victim);
		hand.remove(spell.getId());
		sendCastTargetedSpell(own, spellId, targetId);
	}

	@Override
	public void castSpell(String spellId) {
		CardSpell spell = (CardSpell)hand.get(spellId);
		spell.play();
		hand.remove(spell.getId());
		sendCastUntargetedSpell(spellId);
	}
	
	@Override
	public void heroSpecial(boolean own, String targetId) { // À terminer
		IEntity victim;
		if("hero".equals(targetId)) {
			victim = (own ? this : opponent).getHero();
		} else {
			victim = (own ? this : opponent).getBoard().get(targetId);
		}
		
		hero.special(victim);
		sendHeroTargetedSpecial(own, targetId);
	}

	@Override
	public void heroSpecial() {
		hero.special();
		sendHeroUntargetedSpecial();
	}


	@Override
	public void nextTurn() {
		manaMax++;
		mana = manaMax;
		Card drawn = drawCard(false);
		sendNextTurn(drawn.getName(), drawn.getId(), drawn instanceof CardMinion ? "minion" : "spell");
	}

	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getSessionId() {
		return sessionId;
	}
	
	@Override
	public Set<Card> getDeck() {
		return deck;
	}
	
	@Override
	public Map<String, Card> getHand() {
		return hand;
	}
	
	@Override
	public Map<String, CardMinion> getBoard() {
		return board;
	}
	
	@Override
	public Hero getHero() {
		return hero;
	}
	
	@Override
	public IPlayer getOpponent() {
		return opponent;
	}
	
	@Override
	public int getManaMax() {
		return manaMax;
	}
	
	@Override
	public int getMana() {
		return mana;
	}

	@Override
	public void checkDead() {
		Iterator<Map.Entry<String,CardMinion>> i = this.board.entrySet().iterator();

		while(i.hasNext()) {
			if(i.next().getValue().isDead()) {
				i.remove();
			}
		}

		if(getOpponent().getHero().getHealth() <= 0) {
			// TODO: WIN
		}
	}


	@Override
	public void sendSummonMinion(String minionId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", name);
		send.put("cardId", minionId);
		template.convertAndSend("/topic/game/" + gameId + "/summonMinion", JSONeur.toJSON(send));
	}

	@Override
	public void sendAttack(String cardId, String targetId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", name);
		send.put("cardId", cardId);
		send.put("targetId", targetId);
		template.convertAndSend("/topic/game/" + gameId + "/attack", JSONeur.toJSON(send));
	}

	@Override
	public void sendCastTargetedSpell(boolean own, String spellId, String targetId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", name);
		send.put("cardId", spellId);
		send.put("targetId", targetId);
		send.put("own", own ? "true" : "false");
		template.convertAndSend("/topic/game/" + gameId + "/castTargetedSpell", JSONeur.toJSON(send));
	}

	@Override
	public void sendCastUntargetedSpell(String spellId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", name);
		send.put("cardId", spellId);
		template.convertAndSend("/topic/game/" + gameId + "/castUntargetedSpell", JSONeur.toJSON(send));
	}

	@Override
	public void sendHeroTargetedSpecial(boolean own, String targetId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", name);
		send.put("own", own ? "true" : "false");
		send.put("targetId", targetId);
		template.convertAndSend("/topic/game/" + gameId + "/targetedSpecial", JSONeur.toJSON(send));
	}

	@Override
	public void sendHeroUntargetedSpecial() {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", name);
		template.convertAndSend("/topic/game/" + gameId + "/untargetedSpecial", JSONeur.toJSON(send));
	}

	@Override
	public void sendNextTurn(String cardName, String cardId, String cardType) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", getOpponent().getName());
		send.put("cardName", cardName);
		send.put("cardId", cardId);
		template.convertAndSend("/topic/game/" + gameId + "/nextTurn", JSONeur.toJSON(send));
	}

	@Override
	public void sendTimeout() {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", name);
		template.convertAndSend("/topic/game/" + gameId + "/timeout", JSONeur.toJSON(send));
	}

	@Override
	public void sendDrawCard(String cardName, String cardId, String cardType) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", name);
		send.put("cardName", cardName);
		send.put("cardId", cardId);
		send.put("cardType", cardType);
		template.convertAndSend("/topic/game/" + gameId + "/drawCard", JSONeur.toJSON(send));
	}

	@Override
	public void sendVictory() {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", name);
		template.convertAndSend("/topic/game/" + gameId + "/victory", JSONeur.toJSON(send));
	}


	@Override
	public String toString() {
		Map<String,String> me = new HashMap<>();
		
		me.put("name", name);
		me.put("sessionId", sessionId);
		me.put("deck", deck.toString());
		me.put("hand", hand.toString());
		me.put("board", board.toString());
		me.put("mana", String.valueOf(mana));
		me.put("manaMax", String.valueOf(manaMax));
		me.put("hero", hero.toString());
		me.put("opponent", opponent.getName());

		return JSONeur.toJSON(me);
	}
}