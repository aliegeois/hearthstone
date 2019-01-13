package fr.ministone.game.effect;

import fr.ministone.game.IEntity;

public class SingleTargetLifeBuff extends SingleTargetEffect {
	private int life;
	
	public SingleTargetLifeBuff(int life) {
		super();
		this.life = life;
	}
	
	public void play(IEntity e) {
		e.buffHealth(life);
	}
}