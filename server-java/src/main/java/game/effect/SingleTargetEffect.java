package main.java.game.effect;

import main.java.game.CardSpell;
import main.java.game.Entity;

public abstract class SingleTargetEffect extends Effect{

	public SingleTargetEffect(CardSpell card) {
		super(card);
	}
	
	public void play(Entity e) {}
}
