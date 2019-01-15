package fr.ministone.game.effect;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class SingleTargetEffect extends Effect {
	public SingleTargetEffect() {
		super();
	}

	@Override
	public void play() {
		throw new UnsupportedOperationException();
	}
}
