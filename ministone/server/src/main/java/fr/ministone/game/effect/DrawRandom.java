package fr.ministone.game.effect;

import fr.ministone.game.card.CardSpell;

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
}
