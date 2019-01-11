package game.effect;

import game.CardSpell;
import game.Entity;

public class Transform extends SingleTargetEffect{
	private Entity into;
	
	public Transform(CardSpell card, Entity into) {
		super(card);
		this.into = into;		
	}
	
	@Override
	public void play() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void play(Entity target) {
		target.transform(this.into);
	}
}
