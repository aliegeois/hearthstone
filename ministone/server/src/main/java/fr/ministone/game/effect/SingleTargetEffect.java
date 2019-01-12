package fr.ministone.game.effect;

import fr.ministone.game.CardSpell;
import fr.ministone.game.IEntity;

public abstract class SingleTargetEffect extends Effect{

	public SingleTargetEffect(CardSpell card) {
		super(card);
	}
	
	public abstract void play(IEntity e);
}
