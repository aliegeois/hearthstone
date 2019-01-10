package game.effect;

import game.CardSpell;

public abstract class MultipleTargetEffect extends Effect{
	
	private boolean ownBoard, opponentBoard, ownHero, opponentHero;
	
	public MultipleTargetEffect(CardSpell card, boolean ownBoard, boolean opponentBoard, boolean ownHero, boolean opponentHero) {
		
		super(card);
		this.ownBoard = ownBoard;
		this.opponentBoard = opponentBoard;
		this.ownHero = ownHero;
		this.opponentHero = opponentHero;
	}
	
	public void play() {
	
	}
	
	public boolean isOwnBoard() {
		return this.ownBoard;
	}
	
	public boolean isOpponentBoard() {
		return this.opponentBoard;
	}
	
	public boolean is() {
		return this.ownHero;
	}
	
	public boolean isOpponentHero() {
		return this.opponentHero;
	}
}
