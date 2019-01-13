package fr.ministone.game.effect;

import fr.ministone.game.card.CardSpell;

import javax.persistence.Entity;
import javax.persistence.Transient;

import fr.ministone.game.IEntity;

@Entity
public class Transform extends SingleTargetEffect {
	@Transient
	private IEntity into;
	
	public Transform(CardSpell card, IEntity into) {
		super(card);
		this.into = into;		
	}
	
	@Override
	public void play(IEntity target) {
		target.transform(this.into);
	}
}
