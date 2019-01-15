package fr.ministone.game.effect;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class DrawRandom extends GlobalEffect {
	@JsonProperty
	private int numberCardsDrawn;

	public DrawRandom() {
		super();
	}
	
	public DrawRandom(int numberCardsDrawn) {
		super();
		this.numberCardsDrawn = numberCardsDrawn;
	}
	
	@Override
	public void play() {
		for(int i = 0; i < numberCardsDrawn ; i++)
			card.getOwner().drawCard(true);
	}
}