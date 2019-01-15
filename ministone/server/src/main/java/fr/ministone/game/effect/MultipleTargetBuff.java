package fr.ministone.game.effect;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.ministone.game.card.CardMinion;

@Entity
public class MultipleTargetBuff extends MultipleTargetEffect {
	@JsonProperty
	private int life, attack;
	
	public MultipleTargetBuff() {
		super();
	}
	
	public MultipleTargetBuff(boolean ownBoard, boolean opponentBoard, boolean ownHero, boolean opponentHero, int life, int attack) {
		super(ownBoard, opponentBoard, ownBoard, opponentBoard);
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
