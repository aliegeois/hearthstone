package fr.ministone.game.effect;

import fr.ministone.game.CardSpell;
import fr.ministone.game.IEntity;

public class Transform extends SingleTargetEffect {
	private IEntity into;
	
	public Transform(CardSpell card, IEntity into) {
		super(card);
		this.into = into;		
	}
	
	@Override
	public void play(IEntity target) {
		target.transform(this.into);
	}
}
