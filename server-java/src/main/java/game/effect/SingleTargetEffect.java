package game.effect;

import game.CardSpell;
import game.Entity;

public abstract class SingleTargetEffect extends Effect{

	public SingleTargetEffect(CardSpell card) {
		super(card);
	}
	
	public abstract void play(Entity e);
}
