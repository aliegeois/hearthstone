package game.effect;

import game.CardMinion;
import game.CardSpell;
import game.Entity;

public class MultiTargetDamage extends Effect {
	private boolean ownBoard;
	private boolean opponentBoard;
	private boolean ownHero;
	private boolean opponentHero;
	private int quantity;
	
	public MultiTargetDamage(CardSpell card, boolean ownBoard, boolean opponentBoard, boolean ownHero, boolean opponentHero, int quantity) {
		super(card);
		this.ownBoard = ownBoard;
		this.opponentBoard = opponentBoard;
		this.ownHero = ownHero;
		this.opponentHero = opponentHero;
		this.quantity = quantity;
	}
	
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
	
	public void play(Entity e) {
		throw new UnsupportedOperationException();
	}
}
