package fr.ministone.game.effect;

import fr.ministone.game.card.CardSpell;
import fr.ministone.game.IEntity;

public class SingleTargetHeal extends SingleTargetEffect {
	private int amount;
	
	public SingleTargetHeal(CardSpell card, int amount) {
		super(card);
		this.amount = amount;
	}
	
	@Override
	public void play(IEntity e) {
		e.heal(amount);
	}
}