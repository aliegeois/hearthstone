package fr.ministone.game.effect;

import fr.ministone.game.CardMinion;
import fr.ministone.game.CardSpell;

public class MultiTargetHeal extends MultipleTargetEffect {
	private int quantity;
	
	public MultiTargetHeal(CardSpell card, boolean ownBoard, boolean opponentBoard, boolean ownHero, boolean opponentHero, int amount) {
		super(card, ownBoard, opponentBoard, ownHero, opponentHero);
		this.quantity = amount;
	}
	
	@Override
	public void play() {
		if(ownBoard) {
			for(CardMinion minion : card.getOwner().getBoard().values()) {
			    minion.heal(quantity);
			}
		}
		if(opponentBoard) {
			for(CardMinion minion : card.getOwner().getOpponent().getBoard().values()) {
			    minion.heal(quantity);
			}
		}
		if(ownHero) {
			card.getOwner().getHero().heal(quantity);
		}
		if(opponentHero) {
			card.getOwner().getOpponent().getHero().heal(quantity);
		}
	}
}
