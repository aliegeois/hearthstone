package fr.ministone.game.effect;

import javax.persistence.Entity;

//import fr.ministone.game.card.CardMinion;

@Entity
public class SummonSpecific extends GlobalEffect {
	//private CardMinion card;
	String minionName;
	
	public SummonSpecific(String minionName) {
		super();
		this.minionName = minionName;
	}
	
	@Override
	public void play() {
		card.getOwner().summonMinion(card.getOwner().findMinionByName(minionName));
	}
}
