package fr.ministone.game.effect;

import javax.persistence.Entity;

@Entity
public class DrawRandom extends GlobalEffect {
	private int numberCardsDrawn;
	
	public DrawRandom(int numberCardsDrawn) {
		super();
		this.numberCardsDrawn = numberCardsDrawn;
	}
	
	@Override
	public void play() {
		for(int i = 0; i < numberCardsDrawn ; i++)
			card.getOwner().drawCard();
	}
}
