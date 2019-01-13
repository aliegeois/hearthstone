package fr.ministone.game.effect;

import fr.ministone.game.IEntity;

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
