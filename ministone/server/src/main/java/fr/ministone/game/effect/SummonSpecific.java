package fr.ministone.game.effect;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
public class SummonSpecific extends GlobalEffect {
	@JsonInclude
	private String minionName;
	
	public SummonSpecific() {
		super();
	}

	public SummonSpecific(String minionName) {
		super();
		this.minionName = minionName;
	}
	
	@Override
	public void play() {
		card.getOwner().summonMinion(card.getOwner().findMinionByName(minionName));
	}
}
