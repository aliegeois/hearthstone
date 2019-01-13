package fr.ministone.game.effect;

import fr.ministone.game.card.CardSpell;

public abstract class SingleTargetEffect extends Effect{
	public SingleTargetEffect(CardSpell card) {
		super(card);
	}

	@Override
	public void play() {
		throw new UnsupportedOperationException();
	}
}
