package fr.ministone.game.effect;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import fr.ministone.game.card.CardSpell;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class SingleTargetEffect extends Effect{
	public SingleTargetEffect(CardSpell card) {
		super(card);
	}

	@Override
	public void play() {
		throw new UnsupportedOperationException();
	}
}
