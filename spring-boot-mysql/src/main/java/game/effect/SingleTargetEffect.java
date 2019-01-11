package game.effect;

import game.CardSpell;
import game.Entite;

public abstract class SingleTargetEffect extends Effect{

	public SingleTargetEffect(CardSpell card) {
		super(card);
	}
	
	public void play(Entite e) {}
}
