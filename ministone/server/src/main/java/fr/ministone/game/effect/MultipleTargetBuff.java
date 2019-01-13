package fr.ministone.game.effect;

import fr.ministone.game.card.CardMinion;
import fr.ministone.game.card.CardSpell;

public class MultipleTargetBuff extends MultipleTargetEffect {
	private int life;
	private int attack;
	
	public MultipleTargetBuff(CardSpell card, boolean ownBoard, boolean opponentBoard, boolean ownHero, boolean opponentHero, int life, int attack) {
		super(card, ownBoard, opponentBoard, ownBoard, opponentBoard);
		this.life = life;
		this.attack = attack;
	}
	
	@Override
	public void play() {
		if(ownBoard) {
			for(CardMinion minion : card.getOwner().getBoard().values()) {
				if(life > 0)
					minion.buffHealth(life);
				if(attack > 0)
					minion.buffDamage(attack);
			}
		}
		if(opponentBoard) {
			for(CardMinion minion : card.getOwner().getOpponent().getBoard().values()) {
				if(life > 0)
					minion.buffHealth(life);
				if(attack > 0)
					minion.buffDamage(attack);
			}
		}
	}
}