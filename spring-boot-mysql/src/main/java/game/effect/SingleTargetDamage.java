package game.effect;

import game.CardSpell;
import game.Entite;

public class SingleTargetDamage extends SingleTargetEffect {
	private int damage;
	
	public SingleTargetDamage(CardSpell card, int damage) {
		super(card);
		this.damage = damage;
	}
	
	public void play() {
		throw new UnsupportedOperationException();
	}
	
	public void play(Entite e) {
		e.takeDamage(damage);
	}
}
