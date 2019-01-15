package fr.ministone.game.effect;

import javax.persistence.Entity;

import fr.ministone.game.IEntity;

@Entity
public class Transform extends SingleTargetEffect {
	private String minionName;
	
	public Transform() {
		super();	
	}

	public Transform(String minionName) {
		super();
		this.minionName = minionName;
	}
	
	@Override
	public void play(IEntity target) {
		target.transform(card.getOwner().findMinionByName(minionName));
	}
}
