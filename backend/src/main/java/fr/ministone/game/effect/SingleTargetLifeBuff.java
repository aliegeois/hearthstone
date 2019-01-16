package fr.ministone.game.effect;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.ministone.game.IEntity;

@Entity
public class SingleTargetLifeBuff extends SingleTargetEffect {
	@JsonProperty
	private int quantity;
	
	public SingleTargetLifeBuff() {
		super();
	}

	public SingleTargetLifeBuff(int quantity) {
		super();
		this.quantity = quantity;
	}
	
	public void play(IEntity e) {
		e.buffHealth(quantity);
	}
}