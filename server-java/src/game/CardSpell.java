package game;

import java.util.Set;

import game.effect.Effect;

public class CardSpell extends Card {
	Set<Effect> effects;
	
	CardSpell(int id, Player owner, String name, int mana, Set<Effect> effects) {
		super(id, owner, name, mana);
		this.effects = effects;
	}
	
	void summon(CardMinion target) {
		for(Effect e : effects) {
			
		}
	}
}
