package main.java.game.effect;

import main.java.game.CardSpell;
import main.java.game.Entity;
import main.java.game.hero.Hero;
import main.java.game.CardMinion;

public class SingleTargetDamageBuff extends SingleTargetEffect {
	private int attack;
	
	public SingleTargetDamageBuff(CardSpell card, int attack) {
		super(card);
		this.attack = attack;
	}
	
	@Override
	public void play() {
		throw new UnsupportedOperationException();
	}
	
	public void play(CardMinion e) {
		e.boostDamage(attack);
	}
}
