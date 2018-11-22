package game.effect;

import game.CardSpell;
import game.hero.Hero;
import game.CardMinion;

public class SingleTargetBuff extends Effect {
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
	/*@Override
	public void play(Hero e) {
		
	}*/
	
	/*@Override
	public void play(CardMinion e) {
	
	}*/
}
