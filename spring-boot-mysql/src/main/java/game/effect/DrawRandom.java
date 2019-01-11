package game.effect;

import game.CardSpell;
import game.Entite;

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
	
	void play(Entite e) {
		throw new UnsupportedOperationException();
	}
}
