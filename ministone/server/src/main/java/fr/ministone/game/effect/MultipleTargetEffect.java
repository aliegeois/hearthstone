package fr.ministone.game.effect;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import fr.ministone.game.IEntity;

//@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@MappedSuperclass
public abstract class MultipleTargetEffect extends Effect {
	protected boolean ownBoard, opponentBoard, ownHero, opponentHero;
	
	public MultipleTargetEffect() {
		super();
	}

	public MultipleTargetEffect(boolean ownBoard, boolean opponentBoard, boolean ownHero, boolean opponentHero) {
		super();
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
