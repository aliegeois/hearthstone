package game.effect;

import game.CardSpell;
import game.Entite;
import game.hero.Hero;
import game.CardMinion;

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
	
	public void play(Entite e) {
		e.boostHealth(life);
	}
}