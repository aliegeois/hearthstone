package fr.ministone.game.effect;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.ministone.game.IEntity;

@Entity
public class SingleTargetDamage extends SingleTargetEffect {
	@JsonProperty
	private int quantity;
	
	public SingleTargetDamage() {
		super();
	}

	public SingleTargetDamage(int quantity) {
		super();
		this.quantity = quantity;
	}
	
	@Override
	public void play(IEntity e) {
		e.takeDamage(quantity);
	}
}
