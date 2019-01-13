package fr.ministone.game.effect;

import fr.ministone.game.IEntity;

public class SingleTargetDamage extends SingleTargetEffect {
	private int damage;
	
	public SingleTargetDamage(int damage) {
		super();
		this.damage = damage;
	}
	
	@Override
	public void play(IEntity e) {
		e.takeDamage(damage);
	}
}
