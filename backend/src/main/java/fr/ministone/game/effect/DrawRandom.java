package fr.ministone.game.effect;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class DrawRandom extends GlobalEffect {
	@JsonProperty
	private int quantity;

	public DrawRandom() {
		super();
	}
	
	public DrawRandom(int quantity) {
		super();
		this.quantity = quantity;
	}
	
	@Override
	public void play() {
		for(int i = 0; i < quantity ; i++)
			card.getOwner().drawCard(true);
	}
}
