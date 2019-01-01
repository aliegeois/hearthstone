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
	private int cardId = 0;
	
	private int manaMax;
	private int mana;
	
	private Hero hero;
	private Player opponent;
	
	Player(String name, String heroType) {
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
	
	void setOpponent(Player p) {
		opponent = p;
	}
	
	public void playMinion(int minionId) {
		CardMinion minion = (CardMinion)hand.get(minionId);
		this.board.put(minionId, minion);
		this.hand.remove(minionId);
	}
	
	public void drawCard() {
		hand.put(cardId++, (Card)deck.toArray()[(int)(Math.random() * deck.size())]);
	}
	
	public void attack(CardMinion minion, Entity cible) {
		cible.takeDamage(minion.getDamage());
		minion.takeDamage(cible.getDamage());
		
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