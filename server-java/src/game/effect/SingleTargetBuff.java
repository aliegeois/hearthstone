package game.effect;

import game.CardSpell;
import game.Entity;
import game.hero.Hero;
import game.CardMinion;

public class SingleTargetBuff extends SingleTargetEffect {
	private int life;
	private int attack;
	
	public SingleTargetBuff(CardSpell card, int life, int attack) {
		super(card);
		this.life = life;
		this.attack = attack;
	}
	
	@Override
	public void play() {
		throw new UnsupportedOperationException();
	}
	
	//TODO : voir pourquoi ça ne marche pas
	
	public void play(Hero e) {
		e.boostHealth(life);
	}
	
	public void play(CardMinion e) {
		e.boostHealth(life);
		e.boostDamage(attack);
	}
}
