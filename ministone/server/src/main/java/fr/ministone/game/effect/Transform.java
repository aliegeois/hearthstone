package fr.ministone.game.effect;

import javax.persistence.Entity;
//import javax.persistence.Transient;

import fr.ministone.game.IEntity;
//import fr.ministone.game.card.CardMinion;

@Entity
public class Transform extends SingleTargetEffect {
	//@Transient
	//private IEntity into;
	private String minionName;
	
	/*public Transform(IEntity into) {
		super();
		this.into = into;		
	}*/

	public Transform(String minionName) {
		super();
		this.minionName = minionName;
	}
	
	@Override
	public void play(IEntity target) {
		target.transform(card.getOwner().findMinionByName(minionName));
	}
}
