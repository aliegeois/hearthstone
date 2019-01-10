package main.java.game.effect;

import main.java.game.CardMinion;
import main.java.game.CardSpell;
import main.java.game.Entity;

public class MultiTargetHeal extends MultipleTargetEffect {
	private int amount;
	
	public MultiTargetHeal(CardSpell card, boolean ownBoard, boolean opponentBoard, boolean ownHero, boolean opponentHero, int amount) {
		super(card, ownBoard, opponentBoard, ownHero, opponentHero);
		this.amount = amount;
	}
	
	@Override
	public void play() {
		if(this.ownBoard) {
			for(CardMinion minion : card.getOwner().getBoard().values()) {
			    minion.heal(amount);
			}
		}
		if(this.opponentBoard) {
			for(CardMinion minion : card.getOwner().getOpponent().getBoard().values()) {
			    minion.heal(amount);
			}
		}
		
		if(this.ownHero) {
			card.getOwner().getHero().heal(amount);
		}
		if(this.opponentHero) {
			card.getOwner().getOpponent().getHero().heal(amount);
		}
	}
	
	@Override
	public void play(Entity e) {
		throw new UnsupportedOperationException();
	}
}
