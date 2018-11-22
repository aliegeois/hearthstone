package game.effect;

import game.CardSpell;
import game.Entity;

public class SingleTargetDamage extends Effect {
	private int damage;
	
	public SingleTargetDamage(CardSpell card, int damage) {
		super(card);
		this.damage = damage;
	}
	
	public void summon() {
		throw new UnsupportedOperationException();
	}
	
	public void summon(Entity e) {
		e.takeDamage(damage);
	}
}
