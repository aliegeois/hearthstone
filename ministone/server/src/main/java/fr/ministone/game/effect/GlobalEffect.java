package fr.ministone.game.effect;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import fr.ministone.game.IEntity;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class GlobalEffect extends Effect {
	public GlobalEffect() {
		super();
	}

	@Override
	public void play(IEntity e) { // On suppose qu'un GlobalEffect n'a jamais besoin d'une cible
		throw new UnsupportedOperationException("GlobalEffect.play appelé avec un paramètre: " + e);
	}
}
