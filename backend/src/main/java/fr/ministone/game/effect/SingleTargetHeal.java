package fr.ministone.game.effect;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.ministone.game.IEntity;

@Entity
public class SingleTargetHeal extends SingleTargetEffect {
	@JsonProperty
	private int amount;
	
	public SingleTargetHeal() {
		super();
	}

	public SingleTargetHeal(int amount) {
		super();
		this.amount = amount;
	}
	
	@Override
	public void play(IEntity e) {
		e.heal(amount);
	}
}
