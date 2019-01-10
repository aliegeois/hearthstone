package game.effect;

import game.CardSpell;
import game.Entity;

public class DrawRandom extends GlobalEffect {
	private int numberDrawnCard;
	
	public DrawRandom(CardSpell card, int numberDrawnCard) {
		super(card);
		this.numberDrawnCard = numberDrawnCard;
	}
	
	@Override
	public void play() {
		for(int i = 0; i < this.numberDrawnCard ; i++)
			card.getOwner().drawCard();
	}
	
	void play(Entity e) {
		throw new UnsupportedOperationException();
	}
}
