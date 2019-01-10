package main.java.game.effect;

import main.java.game.CardSpell;
import main.java.game.Entity;

public class SingleTargetDamage extends SingleTargetEffect {
	private int damage;
	
	public SingleTargetDamage(CardSpell card, int damage) {
		super(card);
		this.damage = damage;
	}
	
	public void play() {
		throw new UnsupportedOperationException();
	}
	
	public void play(Entity e) {
		e.takeDamage(damage);
	}
}
