package fr.ministone.game.effect;

import javax.persistence.Entity;

import fr.ministone.game.IEntity;

@Entity
public class SingleTargetDamageBuff extends SingleTargetEffect {
	private int attack;
	
	public SingleTargetDamageBuff() {
		super();
	}

	public SingleTargetDamageBuff(int attack) {
		super();
		this.attack = attack;
	}
	
	public void play(IEntity e) {
		e.buffDamage(attack);
	}
}
