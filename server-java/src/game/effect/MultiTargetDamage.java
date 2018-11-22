package game.effect;

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
	
	@Override
	public void play() {
		if(ownBoard) {
			card.getOwner().getBoard().forEach((i, card) -> {
				card.takeDamage(quantity);
			});
		}
		if(opponentBoard) {
			card.getOwner().getOpponent().getBoard().forEach((i, card) -> {
				card.takeDamage(quantity);
			});
		}
	}
	
	public void play(Entity e) {
		throw new UnsupportedOperationException();
	}
}
