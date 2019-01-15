package fr.ministone.game.effect;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.ministone.game.IEntity;

@Entity
public class SingleTargetLifeBuff extends SingleTargetEffect {
	@JsonProperty
	private int life;
	
	public SingleTargetLifeBuff() {
		super();
	}

	public SingleTargetLifeBuff(int life) {
		super();
		this.life = life;
	}
	
	public void play(IEntity e) {
		e.buffHealth(life);
	}
}