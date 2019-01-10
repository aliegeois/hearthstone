package main.java.game.effect;

import main.java.game.CardMinion;
import main.java.game.CardSpell;
import main.java.game.Entity;

public class MultiTargetBuff extends MultipleTargetEffect {
	private int life;
	private int attack;
	
	public MultiTargetBuff(CardSpell card, boolean ownBoard, boolean opponentBoard, boolean ownHero, boolean opponentHero, int life, int attack) {
		super(card, ownBoard, opponentBoard, ownBoard, opponentBoard);
		this.life = life;
		this.attack = attack;
	}
	
	@Override
	public void play() {
		if(this.ownBoard) {
			for(CardMinion minion : card.getOwner().getBoard().values()) {
			    minion.boostHealth(life);
			    minion.boostDamage(attack);
			}
		}
		if(this.opponentBoard) {
			for(CardMinion minion : card.getOwner().getOpponent().getBoard().values()) {
			    minion.boostHealth(life);
			    minion.boostDamage(attack);
			}
		}
	}
	
	@Override
	public void play(Entity e) {
		throw new UnsupportedOperationException();
	}
}
