package game.effect;

import game.CardSpell;
import game.Entity;

public abstract class Effect {
	protected CardSpell card;
	
	public Effect(CardSpell card) {
		this.card = card;
	}
	
	void summon() {
		
	}
	
	void summon(Entity e) {
		
	}
}
