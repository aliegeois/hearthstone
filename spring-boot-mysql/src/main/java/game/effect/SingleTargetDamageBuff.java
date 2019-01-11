package game.effect;

import game.CardSpell;
import game.Entite;
import game.hero.Hero;
import game.CardMinion;

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
