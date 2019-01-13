package fr.ministone.game.effect;

import fr.ministone.game.card.CardMinion;

public class MultipleTargetDamage extends MultipleTargetEffect {
	private int quantity;
	
	public MultipleTargetDamage(boolean ownBoard, boolean opponentBoard, boolean ownHero, boolean opponentHero, int damage) {
		super(ownBoard, opponentBoard, ownHero, opponentHero);
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
