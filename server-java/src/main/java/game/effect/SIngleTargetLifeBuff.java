package main.java.game.effect;

import main.java.game.CardSpell;
import main.java.game.Entity;
import main.java.game.hero.Hero;
import main.java.game.CardMinion;

public class SIngleTargetLifeBuff extends SingleTargetEffect {
	private int life;
	
	public SIngleTargetLifeBuff(CardSpell card, int life) {
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