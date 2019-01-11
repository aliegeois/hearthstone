package game.effect;

import game.CardSpell;
import game.Entite;

public abstract class Effect {
	protected CardSpell card;
	
	public Effect(CardSpell card) {
		this.card = card;
	}
	
	void play() {
		
	}
	
	void play(Entite e) {
		
	}
}
