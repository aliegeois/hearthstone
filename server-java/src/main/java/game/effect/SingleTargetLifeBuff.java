package game.effect;

import game.CardSpell;
import game.Entity;

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
	
	public void play(Entity e) {
		e.boostHealth(life);
	}
}