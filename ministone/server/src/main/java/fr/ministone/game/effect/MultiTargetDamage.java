package fr.ministone.game.effect;

import fr.ministone.game.CardMinion;
import fr.ministone.game.CardSpell;

public class MultiTargetDamage extends MultipleTargetEffect {
	private int quantity;
	
	public MultiTargetDamage(CardSpell card, boolean ownBoard, boolean opponentBoard, boolean ownHero, boolean opponentHero, int damage) {
		super(card, ownBoard, opponentBoard, ownHero, opponentHero);
		this.quantity = damage;
	}
	
	@Override
	public void play() {
		if(ownBoard) {
			for(CardMinion minion : card.getOwner().getBoard().values()) {
			    minion.takeDamage(quantity);
			}
		}
		if(opponentBoard) {
			for(CardMinion minion : card.getOwner().getOpponent().getBoard().values()) {
			    minion.takeDamage(quantity);
			}
		}
		if(ownHero) {
			card.getOwner().getHero().takeDamage(quantity);
		}
		if(opponentHero) {
			card.getOwner().getOpponent().getHero().takeDamage(quantity);
		}
	}
}
