package fr.ministone.game.effect;

import fr.ministone.game.IEntity;

public abstract class GlobalEffect extends Effect {
	public GlobalEffect() {
		super();
	}

	@Override
	public void play(IEntity e) { // On suppose qu'un GlobalEffect n'a jamais besoin d'une cible
		throw new UnsupportedOperationException("GlobalEffect.play appelé avec un paramètre: " + e);
	}
}
