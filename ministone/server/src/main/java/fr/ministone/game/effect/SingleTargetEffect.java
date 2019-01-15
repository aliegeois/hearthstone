package fr.ministone.game.effect;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

//@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@MappedSuperclass
public abstract class SingleTargetEffect extends Effect{
	public SingleTargetEffect() {
		super();
	}

	@Override
	public void play() {
		throw new UnsupportedOperationException();
	}
}
