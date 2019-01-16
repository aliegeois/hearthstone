package fr.ministone.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
	protected Map<String, Card> hand = new HashMap<>();
	protected Map<String, CardMinion> board = new HashMap<>();
	
	protected int manaMax = 0;
	protected int mana = 0;

	protected boolean ready = false;
	
	protected Hero hero;
	protected IPlayer opponent;
	
	public Player(String name, String sessionId, String gameId, String heroType, AbstractMessageSendingTemplate<String> template, CardMinionRepository cardMinionRepository, CardSpellRepository cardSpellRepository) {
		this.name = name;
		this.sessionId = sessionId;
		this.gameId = gameId;
		this.template = template;
		this.cardMinionRepository = cardMinionRepository;
		this.cardSpellRepository = cardSpellRepository;
		
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
			mana = 1;
			manaMax = 1;
			p.setOpponent(this);
		}
	}

	@Override
	public void readyToStart() {
		ready = true;
	}
	
	@Override
	public void summonMinion(String minionId) {
		CardMinion minion = (CardMinion)hand.get(minionId);
		summonMinion(minion);

	}

	@Override
	public void summonMinion(CardMinion minion) {
		if(looseMana(minion.getManaCost())) {
			hand.remove(minion.getId());
			board.put(minion.getId(), minion);
			minion.play();
			sendSummonMinionFromHand(minion.getId());
		} else {
			// Si on a le temps: faire un message de type "notEnoughMana" et l'envoyer
		}

	}

	@Override
	public void summonMinionByName(String minionName) {
		CardMinion minion = (CardMinion)cardMinionRepository.findByName(minionName).copy(this);
		if(looseMana(minion.getManaCost())) {
			board.put(minion.getId(), minion);
			minion.play();
			sendSummonMinionGlobal(minionName, minion.getId());
		} else {
			// Si on a le temps: faire un message de type "notEnoughMana" et l'envoyer
		}

	}
	
	@Override
	public void attack(boolean isHero, String cardId, String targetId) { // Plus de vérifications (genre opponent card existe ou pas) ??
		CardMinion minion = (CardMinion)board.get(cardId);
		if(minion.isReady()) {
			if(isHero) {
				minion.attack(opponent.getHero());
			} else {
				minion.attack(opponent.getBoard().get(targetId));
			}
			sendAttack(isHero, cardId, targetId); // J'ai un doute sur l'ordre mdr
			minion.setReady(false);
		}
		
	}
	
	@Override
	public Card drawCard(boolean send) {
		Card card = (Card)deck.toArray()[(int)(Math.random() * deck.size())];
		
		return drawCard(card, send);
	}

	@Override
	public Card drawCard(Card card, boolean send) {
		Card cardDrawn = card.copy(this);

		hand.put(cardDrawn.getId(), cardDrawn);
		if(send)
			sendDrawCard(cardDrawn.getName(), cardDrawn.getId(), cardDrawn instanceof CardMinion ? "minion" : "spell");
		
		return cardDrawn;
	}

	@Override
	public void castSpell(boolean own, boolean isHero, String spellId, String targetId) { // À terminer (je crois ?)
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
	public void castSpell(String spellId) {
		CardSpell spell = (CardSpell)hand.get(spellId);
		
		if(looseMana(spell.getManaCost())) {
			spell.play();
			hand.remove(spell.getId());
			sendCastUntargetedSpell(spellId);
		}
	}
	
	@Override
	public void heroSpecial(boolean own, boolean isHero, String targetId) {
		if(!(hero.isUsed())) {
			if(looseMana(Constants.HEROSPECIALCOST)) {
				IEntity victim;

				if(isHero) {
					victim = (own ? this : opponent).getHero();
				} else {
					victim = (own ? this : opponent).getBoard().get(targetId);
				}
				
				hero.special(victim);
				sendHeroTargetedSpecial(own, isHero, targetId);
			}
			hero.setUsed(true);
		}
		
	}

	@Override
	public void heroSpecial() {
		if(!(hero.isUsed())){
			if(looseMana(Constants.HEROSPECIALCOST)) {
				System.out.println("BOOOOOOOOM");
				hero.special();
				sendHeroUntargetedSpecial();
				hero.setUsed(true);
			}
		}
			
	}


	@Override
	public void nextTurn() {
		manaMax = Math.min(manaMax + 1, Constants.PLAYERMANAMAX);
		mana = manaMax;
		Card drawn = drawCard(false);
		getHero().setUsed(false);
		for(CardMinion card: getBoard().values()){
			card.setReady(true);
		}
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
	public boolean isReady() {
		return ready;
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
		//Iterator<Map.Entry<String, CardMinion>> i = board.entrySet().iterator();
		Iterator<CardMinion> i = board.values().iterator();

		while(i.hasNext()) {
			//if(i.next().getValue().isDead()) {
			CardMinion current = i.next();
			if(current.isDead()) {
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
	public void sendSummonMinionFromHand(String minionId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", name);
		send.put("cardId", minionId);
		template.convertAndSend("/topic/game/" + gameId + "/summonMinionFromHand", JsonUtil.toJSON(send));
	}

	@Override
	public void sendSummonMinionGlobal(String minionName, String minionId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", name);
		send.put("cardName", minionName);
		send.put("cardId", minionId);
		template.convertAndSend("/topic/game/" + gameId + "/summonMinionGlobal", JsonUtil.toJSON(send));
	}

	@Override
	public void sendAttack(boolean hero, String cardId, String targetId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", name);
		send.put("hero", hero ? "true" : "false");
		send.put("cardId", cardId);
		send.put("targetId", targetId);
		template.convertAndSend("/topic/game/" + gameId + "/attack", JsonUtil.toJSON(send));
	}

	@Override
	public void sendCastTargetedSpell(boolean own, boolean hero, String spellId, String targetId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", name);
		send.put("own", own ? "true" : "false");
		send.put("hero", hero ? "true" : "false");
		send.put("cardId", spellId);
		send.put("targetId", targetId);
		template.convertAndSend("/topic/game/" + gameId + "/castTargetedSpell", JsonUtil.toJSON(send));
	}

	@Override
	public void sendCastUntargetedSpell(String spellId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", name);
		send.put("cardId", spellId);
		template.convertAndSend("/topic/game/" + gameId + "/castUntargetedSpell", JsonUtil.toJSON(send));
	}

	@Override
	public void sendHeroTargetedSpecial(boolean own, boolean hero, String targetId) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", name);
		send.put("own", own ? "true" : "false");
		send.put("hero", hero ? "true" : "false");
		send.put("targetId", targetId);
		template.convertAndSend("/topic/game/" + gameId + "/targetedSpecial", JsonUtil.toJSON(send));
	}

	@Override
	public void sendHeroUntargetedSpecial() {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", name);
		template.convertAndSend("/topic/game/" + gameId + "/untargetedSpecial", JsonUtil.toJSON(send));
	}

	@Override
	public void sendNextTurn(String cardName, String cardId, String cardType) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", opponent.getName());
		send.put("cardName", cardName);
		send.put("cardType", cardType);
		send.put("cardId", cardId);
		template.convertAndSend("/topic/game/" + gameId + "/nextTurn", JsonUtil.toJSON(send));
	}

	@Override
	public void sendTimeout() {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", name);
		template.convertAndSend("/topic/game/" + gameId + "/timeout", JsonUtil.toJSON(send));
	}

	@Override
	public void sendDrawCard(String cardName, String cardId, String cardType) {
		Map<String,String> send = new HashMap<>();
		send.put("playerName", name);
		send.put("cardName", cardName);
		send.put("cardId", cardId);
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