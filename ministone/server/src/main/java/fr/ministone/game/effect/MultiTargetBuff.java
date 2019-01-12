package fr.ministone.game.effect;

import fr.ministone.game.CardMinion;
import fr.ministone.game.CardSpell;
import fr.ministone.game.IEntity;

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
		if(ownBoard) {
			for(CardMinion minion : card.getOwner().getBoard().values()) {
			    minion.buffHealth(life);
			    minion.buffDamage(attack);
			}
		}
		if(opponentBoard) {
			for(CardMinion minion : card.getOwner().getOpponent().getBoard().values()) {
			    minion.buffHealth(life);
			    minion.buffDamage(attack);
			}
		}
	}
	
	@Override
	public void play(IEntity e) {
		throw new UnsupportedOperationException();
	}
}
