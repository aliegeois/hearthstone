package game.effect;

import game.CardSpell;
import game.Entite;

public class SingleTargetHeal extends SingleTargetEffect {
	private int amount;
	
	public SingleTargetHeal(CardSpell card, int amount) {
		super(card);
		this.amount = amount;
	}
	
	@Override
	public void play() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void play(Entite e) {
		e.heal(amount);
	}
}
