package main.java.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import main.java.game.hero.Hero;
import main.java.game.hero.HeroMage;
import main.java.game.hero.HeroPaladin;
import main.java.game.hero.HeroWarrior;

public class Player {
	private String name;
	private Set<Card> deck = new HashSet<>();
	private Map<String, Card> hand = new HashMap<>();
	private Map<String, CardMinion> board = new HashMap<>();
	
	private int manaMax;
	private int mana;
	
	private Hero hero;
	private Player opponent;
	
	public Player(String name, String heroType) {
		this.name = name;
		switch(heroType) {
		case "mage":
			this.hero = new HeroMage(this);
			break;
		case "paladin":
			this.hero = new HeroPaladin(this);
			break;
		case "warrior":
			this.hero = new HeroWarrior(this);
			break;
		}
		
		this.manaMax = 0;
		this.mana = this.manaMax;
	}
	
	public void setOpponent(Player p) {
		opponent = p;
		if(p.getOpponent() == null){
			p.setOpponent(this);
		}
	}
	
	public void playMinion(String minionId) {
		CardMinion minion = (CardMinion)hand.get(minionId);
		minion.play();
		this.board.put(minionId, minion);
		this.hand.remove(minionId);
	}
	
	public String drawCard() {
		Card cardDrawn = (Card)deck.toArray()[(int)(Math.random() * deck.size())];
		String identif = UUID.randomUUID().toString();

		Card carte = cardDrawn.copy();
		carte.setIdentifiant(identif);
		hand.put(identif, carte);

		return identif;
	}
	
	public void attack(CardMinion minion, Entity cible) {
		minion.attack(cible);
		
	}
	
	void useSpell(String cardId) {
		CardSpell spell = (CardSpell)hand.get(cardId);
		spell.play();
	}
	
	void heroSpecial(Entity target) {
		hero.special(target);
	}
	
	public String getName() {
		return name;
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
}