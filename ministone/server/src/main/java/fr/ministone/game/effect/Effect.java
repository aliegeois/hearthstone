package fr.ministone.game.effect;

import fr.ministone.game.card.CardSpell;
import fr.ministone.game.IEntity;

public abstract class Effect {
	protected CardSpell card;

	public void setCard(CardSpell spell) {
		card = spell;
	}

	public CardSpell getCard() {
		return card;
	}
	
	public abstract void play();
	public abstract void play(IEntity e);
}
