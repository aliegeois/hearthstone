package fr.ministone.game.effect;

import javax.persistence.Entity;

import fr.ministone.game.IEntity;

@Entity
public class SingleTargetHeal extends SingleTargetEffect {
	private int amount;
	
	public SingleTargetHeal(int amount) {
		super();
		this.amount = amount;
	}
	
	@Override
	public void play(IEntity e) {
		e.heal(amount);
	}
}
