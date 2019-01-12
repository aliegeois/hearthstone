package fr.ministone.game.effect;

import fr.ministone.game.CardSpell;
import fr.ministone.game.IEntity;

public class SingleTargetDamage extends SingleTargetEffect {
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
	public void play(IEntity e) {
		e.takeDamage(damage);
	}
}
