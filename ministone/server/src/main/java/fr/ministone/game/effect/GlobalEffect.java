package fr.ministone.game.effect;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import fr.ministone.game.IEntity;

@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@MappedSuperclass
public abstract class GlobalEffect extends Effect {
	public GlobalEffect() {
		super();
	}

	@Override
	public void play(IEntity e) { // On suppose qu'un GlobalEffect n'a jamais besoin d'une cible
		throw new UnsupportedOperationException("GlobalEffect.play appelé avec un paramètre: " + e);
	}
}
