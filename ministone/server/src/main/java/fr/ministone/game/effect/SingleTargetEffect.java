package fr.ministone.game.effect;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class SingleTargetEffect extends Effect{
	public SingleTargetEffect() {
		super();
	}

	@Override
	public void play() {
		throw new UnsupportedOperationException();
	}
}
