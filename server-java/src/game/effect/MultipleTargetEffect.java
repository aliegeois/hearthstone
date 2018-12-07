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
	
	public boolean getOwnBoard() {
		return this.ownBoard;
	}
	
	public boolean getOpponentBoard() {
		return this.opponentBoard;
	}
	
	public boolean getOwnHero() {
		return this.ownHero;
	}
	
	public boolean getOpponentHero() {
		return this.opponentHero;
	}
}
