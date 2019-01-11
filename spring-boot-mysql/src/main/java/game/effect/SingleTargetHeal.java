package game.effect;

import game.CardSpell;
import game.Entity;

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
	public void play(Entity e) {
		e.heal(amount);
	}
}
