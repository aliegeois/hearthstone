package fr.ministone.game.effect;

import fr.ministone.game.CardSpell;
import fr.ministone.game.IEntity;

public class DrawRandom extends GlobalEffect {
	private int numberCardsDrawn;
	
	public DrawRandom(CardSpell card, int numberCardsDrawn) {
		super(card);
		this.numberCardsDrawn = numberCardsDrawn;
	}
	
	@Override
	public void play() {
		for(int i = 0; i < numberCardsDrawn ; i++)
			card.getOwner().drawCard();
	}
	
	@Override
	public void play(IEntity e) {
		throw new UnsupportedOperationException();
	}
}
