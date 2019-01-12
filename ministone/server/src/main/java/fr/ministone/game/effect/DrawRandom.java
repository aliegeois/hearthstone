package fr.ministone.game.effect;

import fr.ministone.game.CardSpell;
import fr.ministone.game.IEntity;

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
	
	void play(IEntity e) {
		throw new UnsupportedOperationException();
	}
}
