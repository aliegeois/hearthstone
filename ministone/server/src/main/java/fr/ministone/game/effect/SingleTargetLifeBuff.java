package fr.ministone.game.effect;

import fr.ministone.game.CardSpell;
import fr.ministone.game.IEntity;

public class SingleTargetLifeBuff extends SingleTargetEffect {
	private int life;
	
	public SingleTargetLifeBuff(CardSpell card, int life) {
		super(card);
		this.life = life;
	}
	
	@Override
	public void play() {
		throw new UnsupportedOperationException();
	}
	
	public void play(IEntity e) {
		e.boostHealth(life);
	}
}