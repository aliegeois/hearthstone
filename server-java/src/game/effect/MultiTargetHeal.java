package game.effect;

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
			card.getOwner().getBoard().forEach((i, card) -> {
				card.heal(amount);
			});
		}
		if(opponentBoard) {
			card.getOwner().getOpponent().getBoard().forEach((i, card) -> {
				card.heal(amount);
			});
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
