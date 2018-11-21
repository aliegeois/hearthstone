package game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class Player {
	String name;
	Set<Card> deck = new HashSet<>();
	Map<Integer, Card> hand = new HashMap<>();
	Map<Integer, CardMinion> board = new HashMap<>();
	
	Hero hero;
	Player opponent;
	
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
}