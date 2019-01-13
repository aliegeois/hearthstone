package fr.ministone.game.effect;

import fr.ministone.game.card.CardSpell;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import fr.ministone.game.IEntity;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class MultipleTargetEffect extends Effect {
	protected boolean ownBoard, opponentBoard, ownHero, opponentHero;
	
	public MultipleTargetEffect(CardSpell card, boolean ownBoard, boolean opponentBoard, boolean ownHero, boolean opponentHero) {
		super(card);
		this.ownBoard = ownBoard;
		this.opponentBoard = opponentBoard;
		this.ownHero = ownHero;
		this.opponentHero = opponentHero;
	}

	@Override
	public void play(IEntity e) {
		throw new UnsupportedOperationException();
	}
	
	public boolean isOwnBoard() {
		return ownBoard;
	}
	
	public boolean isOpponentBoard() {
		return opponentBoard;
	}
	
	public boolean isOwnHero() {
		return ownHero;
	}
	
	public boolean isOpponentHero() {
		return opponentHero;
	}
}
