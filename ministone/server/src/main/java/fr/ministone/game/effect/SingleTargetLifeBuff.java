package fr.ministone.game.effect;

import fr.ministone.game.card.CardSpell;

import javax.persistence.Entity;

import fr.ministone.game.IEntity;

@Entity
public class SingleTargetLifeBuff extends SingleTargetEffect {
	private int life;
	
	public SingleTargetLifeBuff(CardSpell card, int life) {
		super(card);
		this.life = life;
	}
	
	public void play(IEntity e) {
		e.buffHealth(life);
	}
}