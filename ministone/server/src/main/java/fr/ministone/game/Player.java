package fr.ministone.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import fr.ministone.game.hero.*;
import fr.ministone.JSONeur;
import fr.ministone.game.card.*;

public class Player implements IPlayer {
	private String name;
	private String sessionId;
	private Set<Card> deck = new HashSet<>();
	private Map<String, Card> hand = new HashMap<>();
	private Map<String, CardMinion> board = new HashMap<>();
	
	private int manaMax = 0;
	private int mana = 0;
	
	private Hero hero;
	private IPlayer opponent;
	
	public Player(String name, String sessionId, String heroType) {
		this.name = name;
		this.sessionId = sessionId;

		if("warrior".equals(heroType)) {
			this.hero = new HeroWarrior();
		} else if("paladin".equals(heroType)) {
			this.hero = new HeroPaladin();
		} else if("mage".equals(heroType)) {
			this.hero = new HeroMage();
		} else {
			
			System.out.println("Va te faire foutre, cordialement");
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
	}
	
	@Override
	public String drawCard() {
		Card cardDrawn = (Card)deck.toArray()[(int)(Math.random() * deck.size())];
		String identif = UUID.randomUUID().toString();

		Card carte = cardDrawn.copy();
		carte.setId(identif);
		hand.put(identif, carte);

		return identif;
	}

	@Override
	public void castSpell(boolean own, String cardId, String targetId) { // À terminer
		CardSpell spell = (CardSpell)hand.get(cardId);
		IEntity victim;
		if("hero".equals(targetId)) {
			victim = (own ? this : opponent).getHero();
		} else {
			victim = (own ? this : opponent).getBoard().get(targetId);
		}
		spell.play(victim);
		hand.remove(spell.getId());
	}

	@Override
	public void castSpell(String cardId) {
		CardSpell spell = (CardSpell)hand.get(cardId);
		spell.play();
		hand.remove(spell.getId());
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
	}

	@Override
	public void heroSpecial() {
		hero.special();
	}


	@Override
	public void nextTurn() {
		manaMax++;
		mana = manaMax;
		drawCard();
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

	/*@Override
	public void setHero(String heroType) {
		if("warrior".equals(heroType)) {
			hero = new HeroWarrior();
		} else if("paladin".equals(heroType)){
			hero = new HeroPaladin();
		} else if("mage".equals(heroType)){
			hero = new HeroMage();
		} else {
			// TODO: À enlever
			System.out.println("Va te faire foutre, cordialement");
		}
		hero.setPlayer(this);
	}*/

	@Override
	public void checkDead() {
		Iterator<Map.Entry<String,CardMinion>> i = this.board.entrySet().iterator();

		while(i.hasNext()) {
			if(i.next().getValue().isDead()) {
				i.remove();
			}
		}
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