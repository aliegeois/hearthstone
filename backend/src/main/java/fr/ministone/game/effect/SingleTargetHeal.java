package fr.ministone.game.effect;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.ministone.game.IEntity;

@Entity
public class SingleTargetHeal extends SingleTargetEffect {
	@JsonProperty
	private int quantity;
	
	public SingleTargetHeal() {
		super();
	}

	public SingleTargetHeal(int quantity) {
		super();
		this.quantity = quantity;
	}
	
	@Override
	public void play(IEntity e) {
		e.heal(quantity);
	}
}
