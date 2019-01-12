package fr.ministone.game.effect;

import fr.ministone.game.CardSpell;

public abstract class MultipleTargetEffect extends Effect {
	protected boolean ownBoard, opponentBoard, ownHero, opponentHero;
	
	public MultipleTargetEffect(CardSpell card, boolean ownBoard, boolean opponentBoard, boolean ownHero, boolean opponentHero) {
		super(card);
		this.ownBoard = ownBoard;
		this.opponentBoard = opponentBoard;
		this.ownHero = ownHero;
		this.opponentHero = opponentHero;
	}
	
	public boolean isOwnBoard() {
		return ownBoard;
	}
	
	public boolean isOpponentBoard() {
		return opponentBoard;
	}
	
	public boolean is() {
		return ownHero;
	}
	
	public boolean isOpponentHero() {
		return opponentHero;
	}
}
