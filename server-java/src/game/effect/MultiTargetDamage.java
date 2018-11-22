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
	
	public void summon() {
		if(ownBoard) {
			card.owner;
		}
	}
	
	public void summon(Entity e) {
		throw new UnsupportedOperationException();
	}
}
