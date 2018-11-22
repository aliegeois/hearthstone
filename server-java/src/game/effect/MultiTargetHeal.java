package game.effect;

import game.CardMinion;
import game.CardSpell;
import game.Entity;

public class MultiTargetHeal extends Effect {
	private boolean ownBoard;
	private boolean opponentBoard;
	private boolean ownHero;
	private boolean opponentHero;
	private int amount;
	
	public MultiTargetHeal(CardSpell card, boolean ownBoard, boolean opponentBoard, boolean ownHero, boolean opponentHero, int amount) {
		super(card);
		this.ownBoard = ownBoard;
		this.opponentBoard = opponentBoard;
		this.ownHero = ownHero;
		this.opponentHero = opponentHero;
		this.amount = amount;
	}
	
	@Override
	public void play() {
		if(ownBoard) {
			for(CardMinion minion : card.getOwner().getBoard().values()) {
			    minion.heal(amount);
			}
		}
		if(opponentBoard) {
			for(CardMinion minion : card.getOwner().getOpponent().getBoard().values()) {
			    minion.heal(amount);
			}
		}
		
		if(ownHero) {
			card.getOwner().getHero().heal(amount);
		}
		if(opponentHero) {
			card.getOwner().getOpponent().getHero().heal(amount);
		}
	}
	
	@Override
	public void play(Entity e) {
		throw new UnsupportedOperationException();
	}
}
