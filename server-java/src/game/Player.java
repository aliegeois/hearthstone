package game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import game.hero.Hero;
import game.hero.HeroMage;
import game.hero.HeroPaladin;
import game.hero.HeroWarrior;

public class Player {
	private String name;
	private Set<Card> deck = new HashSet<>();
	private Map<Integer, Card> hand = new HashMap<>();
	private Map<Integer, CardMinion> board = new HashMap<>();
	
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
	}
	
	public void setOpponent(Player p) {
		opponent = p;
		if(p.getOpponent() == null){
			p.setOpponent(this);
		}
	}
	
	public void playMinion(int minionId) {
		CardMinion minion = (CardMinion)hand.get(minionId);
		minion.play();
		this.board.put(minionId, minion);
		this.hand.remove(minionId);
	}
	
	public void drawCard() {
		Card cardDrawn = (Card)deck.toArray()[(int)(Math.random() * deck.size())];
		hand.put(cardDrawn.getId(), cardDrawn);
	}
	
	public void attack(CardMinion minion, Entity cible) {
		minion.attack(cible);
		
	}
	
	void useSpell(int cardId) {
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
	
	public Map<Integer, Card> getHand() {
		return hand;
	}
	
	public Map<Integer, CardMinion> getBoard() {
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