package fr.ministone.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import fr.ministone.game.hero.Hero;

public class Player {
	private String name;
	private String sessionId;
	private Set<Card> deck = new HashSet<>();
	private Map<String, Card> hand = new HashMap<>();
	private Map<String, CardMinion> board = new HashMap<>();
	
	private int manaMax;
	private int mana;
	
	private Hero hero;
	private Player opponent;
	
	public Player(String name, String sessionId) {
		this.name = name;
		this.sessionId = sessionId;
		this.manaMax = 0;
		this.mana = this.manaMax;
	}
	
	public void setOpponent(Player p) {
		opponent = p;
		if(p.getOpponent() == null) {
			p.setOpponent(this);
		}
	}
	
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
	
	public void attack(String cardId, String targetId) { // Plus de vérifications (genre opponent card existe ou pas) ??
		CardMinion minion = (CardMinion)hand.get(cardId);
		if(targetId.equals("hero")) {
			minion.attack(opponent.hero);
		} else {
			minion.attack(opponent.board.get(targetId));
		}
		checkDead();
	}
	
	public String drawCard() {
		Card cardDrawn = (Card)deck.toArray()[(int)(Math.random() * deck.size())];
		String identif = UUID.randomUUID().toString();

		Card carte = cardDrawn.copy();
		carte.setIdentifiant(identif);
		hand.put(identif, carte);

		return identif;
	}

	public void castSpell(boolean own, String cardId, String targetId) { // À terminer
		CardSpell spell = (CardSpell)hand.get(cardId);
		IEntity victim;
		if("hero".equals(targetId)) {
			
		} else {

		}
		//spell.play();
		//this.getHand().remove(spell.getId());
	}

	public void castSpell(String cardId) {
		CardSpell spell = (CardSpell)hand.get(cardId);
		spell.play();
		this.getHand().remove(spell.getId());
	}
	
	public void heroSpecial(boolean own, String targetId) { // À terminer
		IEntity victim = null;
		if(own) {

		} else {

		}
		
		hero.special(victim);
	}

	public void heroSpecial(){
		hero.special();
	}
	
	public String getName() {
		return name;
	}

	public String getSessionId() {
		return sessionId;
	}
	
	public Set<Card> getDeck() {
		return deck;
	}
	
	public Map<String, Card> getHand() {
		return hand;
	}
	
	public Map<String, CardMinion> getBoard() {
		return board;
	}
	
	public Hero getHero() {
		return hero;
	}
	
	public Player getOpponent() {
		return opponent;
	}
	
	public int getManaMax() {
		return manaMax;
	}
	
	public int getMana() {
		return mana;
	}

	public void setHero(Hero hero) {
		this.hero = hero;
	}

	public void checkDead() {
		Iterator<Map.Entry<String,CardMinion>> i = this.board.entrySet().iterator();

		while(i.hasNext()) {
			if(i.next().getValue().isDead()) {
				i.remove();
			}
		}
	}
}