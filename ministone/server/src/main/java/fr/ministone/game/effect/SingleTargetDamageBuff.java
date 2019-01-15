package fr.ministone.game.effect;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.ministone.game.IEntity;

@Entity
public class SingleTargetDamageBuff extends SingleTargetEffect {
	@JsonProperty
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
