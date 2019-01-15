package fr.ministone.game.effect;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.ministone.game.card.CardMinion;

@Entity
public class MultipleTargetDamage extends MultipleTargetEffect {
	@JsonProperty
	private int quantity;
	
	public MultipleTargetDamage() {
		super();
	}

	public MultipleTargetDamage(boolean ownBoard, boolean opponentBoard, boolean ownHero, boolean opponentHero, int damage) {
		super(ownBoard, opponentBoard, ownHero, opponentHero);
		this.quantity = damage;
	}
	
	@Override
	public void play() {
		if(ownBoard) {
			for(CardMinion minion : card.getOwner().getBoard().values()) {
			    minion.takeDamage(quantity);
			}
		}
		if(opponentBoard) {
			for(CardMinion minion : card.getOwner().getOpponent().getBoard().values()) {
			    minion.takeDamage(quantity);
			}
		}
		if(ownHero) {
			card.getOwner().getHero().takeDamage(quantity);
		}
		if(opponentHero) {
			card.getOwner().getOpponent().getHero().takeDamage(quantity);
		}
	}
}
