package fr.ministone.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
//import java.util.Optional;
import java.util.Set;
import java.util.UUID;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.AbstractMessageSendingTemplate;

import fr.ministone.repository.*;

import fr.ministone.game.hero.*;
import fr.ministone.JsonUtil;
import fr.ministone.game.card.*;

public class Player implements IPlayer, IPlayerMessageSender {
	protected String name;
	protected String sessionId;
	protected String gameId;

	protected AbstractMessageSendingTemplate<String> template;
	protected CardMinionRepository cardMinionRepository;
	protected CardSpellRepository cardSpellRepository;

	protected Set<Card> deck = new HashSet<>();
	protected Map<Long, Card> hand = new HashMap<>();
	protected Map<Long, CardMinion> board = new HashMap<>();
	
	protected int manaMax = 0;
	protected int mana = 0;
	
	protected Hero hero;
	protected IPlayer opponent;
	
	public Player(String name, String sessionId, String gameId, String heroType, AbstractMessageSendingTemplate<String> template, CardMinionRepository cardMinionRepository, CardSpellRepository cardSpellRepository) {
		this.name = name;
		this.sessionId = sessionId;
		this.gameId = gameId;
		this.template = template;
		this.cardMinionRepository = cardMinionRepository;
		this.cardSpellRepository = cardSpellRepository;
		
		System.out.println("HEROTYPE recu : " + heroType);
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

		//new UUID().getLeastSignificantBits()
	}

	public Player(String name, String sessionId, String gameId, String heroType) {
		this.name = name;
		this.sessionId = sessionId;
		this.gameId = gameId;
		
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
	public void summonMinion(Long minionId) {
		CardMinion minion = (CardMinion)hand.get(minionId);
		summonMinion(minion);
	}

	@Override
	public void summonMinion(CardMinion minion) {
		if(looseMana(minion.getManaCost())) {
			minion.play();
			board.put(minion.getId(), minion);
			hand.remove(minion.getId());
			sendSummonMinion(minion.getId());
		} else {
			// Si on a le temps: faire un message de type "notEnoughMana" et l'envoyer
		}
	}

	@Override
	public void summonMinion(String minionName) {
		CardMinion minion = cardMinionRepository.findByName(minionName);
		summonMinion(minion);
	}
	
	@Override
	public void attack(boolean isHero, Long cardId, Long targetId) { // Plus de vérifications (genre opponent card existe ou pas) ??
		CardMinion minion = (CardMinion)board.get(cardId);
		if(isHero) {
			minion.attack(opponent.getHero());
		} else {
			minion.attack(opponent.getBoard().get(targetId));
		}
		sendAttack(isHero, cardId, targetId); // J'ai un doute sur l'ordre mdr
	}
	
	@Override
	public Card drawCard(boolean send) {
		System.out.println("Draw card : " + deck.size());
		Card card = (Card)deck.toArray()[(int)(Math.random() * deck.size())];
		
		return drawCard(card, send);
	}

	@Override
	public Card drawCard(Card card, boolean send) {
		Card cardDrawn = card.copy();
		Long cId = UUID.randomUUID().getLeastSignificantBits();

		cardDrawn.setId(cId);
		hand.put(cId, cardDrawn);
		System.out.print("nb de carte" + hand.size());
		if(send)
			sendDrawCard(cardDrawn.getName(), cId, cardDrawn instanceof CardMinion ? "minion" : "spell");
		
		return cardDrawn;
	}

	@Override
	public void castSpell(boolean own, boolean isHero, Long spellId, Long targetId) { // À terminer (je crois ?)
		CardSpell spell = (CardSpell)hand.get(spellId);
		IEntity victim;
		if(isHero) {
			victim = (own ? this : opponent).getHero();
		} else {
			victim = (own ? this : opponent).getBoard().get(targetId);
		}
		if(looseMana(spell.getManaCost())) {
			spell.play(victim);
			hand.remove(spell.getId());
			sendCastTargetedSpell(own, isHero, spellId, targetId);
		}
		
	}

	@Override
	public void castSpell(Long spellId) {
		CardSpell spell = (CardSpell)hand.get(spellId);
		
		if(looseMana(spell.getManaCost())) {
			spell.play();
			hand.remove(spell.getId());
			sendCastUntargetedSpell(spellId);
		}
		
	}
	
	@Override
	public void heroSpecial(boolean own, boolean isHero, Long targetId) {
		IEntity victim;
		if(looseMana(Constants.HEROSPECIALCOST)){
			if(isHero) {
				victim = (own ? this : opponent).getHero();
			} else {
				victim = (own ? this : opponent).getBoard().get(targetId);
			}
			
			hero.special(victim);
			sendHeroTargetedSpecial(own, isHero, targetId);
		}
	}

	@Override
	public void heroSpecial() {
		if(looseMana(Constants.HEROSPECIALCOST)) {
			hero.special();
			sendHeroUntargetedSpecial();
		}
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
	public Map<Long, Card> getHand() {
		return hand;
	}
	
	@Override
	public Map<Long, CardMinion> getBoard() {
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
	public boolean looseMana(int quantity) {
		if(mana >= quantity) {
			mana -= quantity;
			return true;
		}
		return false;
	}

	@Override
	public boolean checkDead() {
		Iterator<Map.Entry<Long,CardMinion>> i = this.board.entrySet().iterator();

		while(i.hasNext()) {
			if(i.next().getValue().isDead()) {
				i.remove();
			}
		}

		return (this.getHero().getHealth() <= 0);
	}

	@Override
	public CardMinion findMinionByName(String name) {
		return cardMinionRepository.findByName(name);
	}


	@Override
	public void sendSummonMinion(Long minionId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", name);
		send.put("cardId", minionId.toString());
		template.convertAndSend("/topic/game/" + gameId + "/summonMinion", JsonUtil.toJSON(send));
	}

	@Override
	public void sendAttack(boolean hero, Long cardId, Long targetId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", name);
		send.put("hero", hero ? "true" : "false");
		send.put("cardId", cardId.toString());
		send.put("targetId", targetId.toString());
		template.convertAndSend("/topic/game/" + gameId + "/attack", JsonUtil.toJSON(send));
	}

	@Override
	public void sendCastTargetedSpell(boolean own, boolean hero, Long spellId, Long targetId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", name);
		send.put("own", own ? "true" : "false");
		send.put("hero", hero ? "true" : "false");
		send.put("cardId", spellId.toString());
		send.put("targetId", targetId.toString());
		template.convertAndSend("/topic/game/" + gameId + "/castTargetedSpell", JsonUtil.toJSON(send));
	}

	@Override
	public void sendCastUntargetedSpell(Long spellId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", name);
		send.put("cardId", spellId.toString());
		template.convertAndSend("/topic/game/" + gameId + "/castUntargetedSpell", JsonUtil.toJSON(send));
	}

	@Override
	public void sendHeroTargetedSpecial(boolean own, boolean hero, Long targetId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", name);
		send.put("own", own ? "true" : "false");
		send.put("hero", hero ? "true" : "false");
		send.put("targetId", targetId.toString());
		template.convertAndSend("/topic/game/" + gameId + "/targetedSpecial", JsonUtil.toJSON(send));
	}

	@Override
	public void sendHeroUntargetedSpecial() {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", name);
		template.convertAndSend("/topic/game/" + gameId + "/untargetedSpecial", JsonUtil.toJSON(send));
	}

	@Override
	public void sendNextTurn(String cardName, Long cardId, String cardType) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", opponent.getName());
		send.put("cardName", cardName);
		send.put("cardId", cardId.toString());
		template.convertAndSend("/topic/game/" + gameId + "/nextTurn", JsonUtil.toJSON(send));
	}

	@Override
	public void sendTimeout() {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", name);
		template.convertAndSend("/topic/game/" + gameId + "/timeout", JsonUtil.toJSON(send));
	}

	@Override
	public void sendDrawCard(String cardName, Long cardId, String cardType) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", name);
		send.put("cardName", cardName);
		send.put("cardId", cardId.toString());
		send.put("cardType", cardType);
		template.convertAndSend("/topic/game/" + gameId + "/drawCard", JsonUtil.toJSON(send));
	}

	@Override
	public void sendVictory() {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", name);
		template.convertAndSend("/topic/game/" + gameId + "/victory", JsonUtil.toJSON(send));
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

		return JsonUtil.toJSON(me);
	}
}