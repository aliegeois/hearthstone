package fr.ministone.game.effect;

import fr.ministone.game.card.CardSpell;

import javax.persistence.Entity;

import fr.ministone.game.IEntity;

@Entity
public class SingleTargetDamage extends SingleTargetEffect {
	private int damage;
	
	public SingleTargetDamage(CardSpell card, int damage) {
		super(card);
		this.damage = damage;
	}
	
	@Override
	public void play(IEntity e) {
		e.takeDamage(damage);
	}
}
