package game.effect;

import game.CardSpell;
import game.Entity;

public class MultiTargetDamage extends Effect {
	private boolean ownBoard;
	private boolean opponentBoard;
	private boolean ownHero;
	private boolean opponentHero;
	private int damage;
	
	public MultiTargetDamage(CardSpell card, boolean ownBoard, boolean opponentBoard, boolean ownHero, boolean opponentHero, int damage) {
		super(card);
		this.ownBoard = ownBoard;
		this.opponentBoard = opponentBoard;
		this.ownHero = ownHero;
		this.opponentHero = opponentHero;
		this.damage = damage;
	}
	
	@Override
	public void play() {
		if(ownBoard) {
			card.getOwner().getBoard().forEach((i, card) -> {
				card.takeDamage(damage);
			});
		}
		if(opponentBoard) {
			card.getOwner().getOpponent().getBoard().forEach((i, card) -> {
				card.takeDamage(damage);
			});
		}
		
		if(ownHero) {
			card.getOwner().getHero().takeDamage(damage);
		}
		if(opponentHero) {
			card.getOwner().getOpponent().getHero().takeDamage(damage);
		}
	}
	
	@Override
	public void play(Entity e) {
		throw new UnsupportedOperationException();
	}
}
