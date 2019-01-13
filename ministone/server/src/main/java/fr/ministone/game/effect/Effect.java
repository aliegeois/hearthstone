package fr.ministone.game.effect;

import fr.ministone.game.card.CardSpell;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import fr.ministone.game.IEntity;

@MappedSuperclass
public abstract class Effect {

	@Id
	@GeneratedValue
	private String id;

	@Transient
	protected CardSpell card;

	public void setCard(CardSpell spell) {
		card = spell;
	}

	public CardSpell getCard() {
		return card;
	}
	
	public abstract void play();
	public abstract void play(IEntity e);
}
