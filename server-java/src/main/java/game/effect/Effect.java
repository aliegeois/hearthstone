package main.java.game.effect;

import main.java.game.CardSpell;
import main.java.game.Entity;

public abstract class Effect {
	protected CardSpell card;
	
	public Effect(CardSpell card) {
		this.card = card;
	}
	
	void play() {
		
	}
	
	void play(Entity e) {
		
	}
}
