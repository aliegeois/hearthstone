package game.effect;

import game.CardSpell;
import game.Entity;

public class MultiTargetBuff extends Effect {
	private boolean ownBoard;
	private boolean opponentBoard;
	private int life;
	private int attack;
	
	public MultiTargetBuff(CardSpell card, boolean ownBoard, boolean opponentBoard, int life, int attack) {
		super(card);
		this.ownBoard = ownBoard;
		this.opponentBoard = opponentBoard;
		this.life = life;
		this.attack = attack;
	}
	
	@Override
	public void play() {
		if(ownBoard) {
			card.getOwner().getBoard().forEach((i, card) -> {
				card.boostHealth(life);
				card.boostDamage(attack);
			});
		}
		if(opponentBoard) {
			card.getOwner().getOpponent().getBoard().forEach((i, card) -> {
				card.boostHealth(life);
				card.boostDamage(attack);
			});
		}
	}
	
	@Override
	public void play(Entity e) {
		throw new UnsupportedOperationException();
	}
}
