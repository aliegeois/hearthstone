package fr.ministone.game.effect;

import javax.persistence.Entity;

import fr.ministone.game.card.CardMinion;
import fr.ministone.game.card.CardSpell;

@Entity
public class MultipleTargetHeal extends MultipleTargetEffect {
	private int quantity;
	
	public MultipleTargetHeal(CardSpell card, boolean ownBoard, boolean opponentBoard, boolean ownHero, boolean opponentHero, int amount) {
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
