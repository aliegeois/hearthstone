package fr.ministone.game.effect;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.ministone.game.card.CardMinion;

@Entity
public class MultipleTargetHeal extends MultipleTargetEffect {
	@JsonProperty
	private int quantity;
	
	public MultipleTargetHeal() {
		super();
	}

	public MultipleTargetHeal(boolean ownBoard, boolean opponentBoard, boolean ownHero, boolean opponentHero, int quantity) {
		super(ownBoard, opponentBoard, ownHero, opponentHero);
		this.quantity = quantity;
	}
	
	@Override
	public void play() {
		if(ownBoard) {
			for(CardMinion minion : card.getOwner().getBoard().values()) {
			    minion.heal(quantity);
			}
		}
		if(opponentBoard) {
			for(CardMinion minion : card.getOwner().getOpponent().getBoard().values()) {
			    minion.heal(quantity);
			}
		}
		if(ownHero) {
			card.getOwner().getHero().heal(quantity);
		}
		if(opponentHero) {
			card.getOwner().getOpponent().getHero().heal(quantity);
		}
	}
}
