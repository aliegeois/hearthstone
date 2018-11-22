package game.effect;

import game.CardSpell;
import game.Entity;

public class SingleTargetDamage extends Effect {
	private int damage;
	
	public SingleTargetDamage(CardSpell card, int damage) {
		super(card);
		this.damage = damage;
	}
	
	@Override
	public void play() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void play(Entity e) {
		e.takeDamage(damage);
	}
}
