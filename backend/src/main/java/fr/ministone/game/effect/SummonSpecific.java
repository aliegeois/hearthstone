package fr.ministone.game.effect;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class SummonSpecific extends GlobalEffect {
	@JsonProperty
	private String minionName;
	@JsonProperty
	private int quantity;
	
	public SummonSpecific() {
		super();
	}

	public SummonSpecific(String minionName, int quantity) {
		super();
		this.minionName = minionName;
		this.quantity = quantity;
	}
	
	@Override
	public void play() {
		for(int i = 0; i < quantity; i++)
			card.getOwner().summonMinionByName(minionName);
	}
}
