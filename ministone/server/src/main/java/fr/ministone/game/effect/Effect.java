package fr.ministone.game.effect;

import fr.ministone.game.CardSpell;
import fr.ministone.game.IEntity;

public abstract class Effect {
	protected CardSpell card;
	
	public Effect(CardSpell card) {
		this.card = card;
	}
	
	public abstract void play();
	public abstract void play(IEntity e);
}
