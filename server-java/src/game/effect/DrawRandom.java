package game.effect;

import game.CardSpell;
import game.Entity;

public class DrawRandom extends Effect {
	private int cardNumber;
	
	public DrawRandom(CardSpell card, int cardNumber) {
		super(card);
		this.cardNumber = cardNumber;
	}
	
	void play() {
		for(int i = 0; i < cardNumber; i++)
			card.getOwner().drawCard();
	}
	
	void play(Entity e) {
		throw new UnsupportedOperationException();
	}
}
