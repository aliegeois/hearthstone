package fr.ministone.game.effect;

import javax.persistence.Entity;
import javax.persistence.Transient;

import fr.ministone.game.IEntity;

@Entity
public class Transform extends SingleTargetEffect {
	@Transient
	private IEntity into;
	
	public Transform(IEntity into) {
		super();
		this.into = into;		
	}
	
	@Override
	public void play(IEntity target) {
		target.transform(into);
	}
}
