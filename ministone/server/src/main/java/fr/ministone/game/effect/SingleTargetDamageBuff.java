package fr.ministone.game.effect;

import fr.ministone.game.IEntity;

public class SingleTargetDamageBuff extends SingleTargetEffect {
	private int attack;
	
	public SingleTargetDamageBuff(int attack) {
		super();
		this.attack = attack;
	}
	
	public void play(IEntity e) {
		e.buffDamage(attack);
	}
}
