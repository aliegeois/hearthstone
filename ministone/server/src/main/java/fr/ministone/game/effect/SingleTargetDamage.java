package fr.ministone.game.effect;

import javax.persistence.Entity;

import fr.ministone.game.IEntity;

@Entity
public class SingleTargetDamage extends SingleTargetEffect {
	private int damage;
	
	public SingleTargetDamage() {
		super();
	}

	public SingleTargetDamage(int damage) {
		super();
		this.damage = damage;
	}
	
	@Override
	public void play(IEntity e) {
		e.takeDamage(damage);
	}
}
