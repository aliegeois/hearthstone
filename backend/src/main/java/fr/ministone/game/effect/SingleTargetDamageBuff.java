package fr.ministone.game.effect;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.ministone.game.IEntity;

@Entity
public class SingleTargetDamageBuff extends SingleTargetEffect {
	@JsonProperty
	private int quantity;
	
	public SingleTargetDamageBuff() {
		super();
	}

	public SingleTargetDamageBuff(int quantity) {
		super();
		this.quantity = quantity;
	}
	
	public void play(IEntity e) {
		e.buffDamage(quantity);
	}
}
