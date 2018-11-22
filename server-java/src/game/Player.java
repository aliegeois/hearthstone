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
	
	void playMinion(int cardId) {
		CardMinion minion = (CardMinion)hand.get(cardId);
		hand.remove(cardId);
		board.put(cardId, minion);
		minion.summon();
	}
	
	void attackMinion(int minionId1, int minionId2) {
		CardMinion minion1 = board.get(minionId1),
		           minion2 = board.get(minionId2);
		minion1.attackMinion(minion2);
	}
	
	void useSpell(int cardId, CardMinion target) {
		CardSpell spell = (CardSpell)hand.get(cardId);
		spell.summon(target);
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
	
	public  Map<Integer, Card> getHand() {
		return hand;
	}
	
	Map<Integer, CardMinion> getBoard() {
		return board;
	}
	
	public Hero getHero() {
		return hero;
	}
}