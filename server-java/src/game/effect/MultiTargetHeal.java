package game.effect;

import game.CardMinion;
import game.CardSpell;
import game.Entity;

public class MultiTargetHeal extends MultipleTargetEffect {
	private int amount;
	
	public MultiTargetHeal(CardSpell card, boolean ownBoard, boolean opponentBoard, boolean ownHero, boolean opponentHero, int amount) {
		super(card, ownBoard, opponentBoard, ownHero, opponentHero);
		this.amount = amount;
	}
	
	@Override
	public void play() {
		if(isOwnBoard()) {
			for(CardMinion minion : card.getOwner().getBoard().values()) {
			    minion.heal(amount);
			}
		}
		if(isOpponentBoard()) {
			for(CardMinion minion : card.getOwner().getOpponent().getBoard().values()) {
			    minion.heal(amount);
			}
		}
		
		if(is()) {
			card.getOwner().getHero().heal(amount);
		}
		if(isOpponentHero()) {
			card.getOwner().getOpponent().getHero().heal(amount);
		}
	}
	
	@Override
	public void play(Entity e) {
		throw new UnsupportedOperationException();
	}
}
