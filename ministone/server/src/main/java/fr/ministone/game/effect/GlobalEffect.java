package fr.ministone.game.effect;

import fr.ministone.game.card.CardSpell;

import javax.persistence.Entity;

import fr.ministone.game.IEntity;

@Entity
public abstract class GlobalEffect extends Effect {
	public GlobalEffect(CardSpell card) {
		super(card);
	}

	@Override
	public void play(IEntity e) { // On suppose qu'un GlobalEffect n'a jamais besoin d'une cible
		throw new UnsupportedOperationException("GlobalEffect.play appelé avec un paramètre: " + e);
	}
}
