package fr.ministone.game.effect;

import fr.ministone.game.card.CardSpell;
import fr.ministone.game.IEntity;

public class SingleTargetDamageBuff extends SingleTargetEffect {
	private int attack;
	
	public SingleTargetDamageBuff(CardSpell card, int attack) {
		super(card);
		this.attack = attack;
	}
	
	public void play(IEntity e) {
		e.buffDamage(attack);
	}
}
