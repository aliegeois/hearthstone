package fr.ministone.game.effect;

import fr.ministone.game.IEntity;

public class Transform extends SingleTargetEffect {
	private IEntity into;
	
	public Transform(IEntity into) {
		super();
		this.into = into;		
	}
	
	@Override
	public void play(IEntity target) {
		target.transform(this.into);
	}
}
