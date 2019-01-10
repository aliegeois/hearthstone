package game.effect;

import game.CardMinion;
import game.CardSpell;
import game.Entity;

public class MultiTargetDamage extends MultipleTargetEffect {
	private int quantity;
	
	public MultiTargetDamage(CardSpell card, boolean ownBoard, boolean opponentBoard, boolean ownHero, boolean opponentHero, int damage) {
		super(card, ownBoard, opponentBoard, ownHero, opponentHero);
		this.quantity = damage;
	}
	
	@Override
	public void play() {
		if(isOwnBoard()) {
			for(CardMinion minion : card.getOwner().getBoard().values()) {
			    minion.takeDamage(quantity);
			}
		}
		if(isOpponentBoard()) {
			for(CardMinion minion : card.getOwner().getOpponent().getBoard().values()) {
			    minion.takeDamage(quantity);
			}
		}
		if(is()) {
			card.getOwner().getHero().takeDamage(quantity);
		}
		if(isOpponentHero()) {
			card.getOwner().getOpponent().getHero().takeDamage(quantity);
		}
	}
	
	public void play(Entity e) {
		throw new UnsupportedOperationException();
	}
}
