package fr.ministone.game.effect;

import fr.ministone.game.card.CardSpell;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.ministone.game.IEntity;

@MappedSuperclass
public abstract class Effect {
	@Id
	@GeneratedValue
	private Long id;

	@Transient
	@JsonIgnore
	protected CardSpell card;

	protected final String type;

	public Effect() {
		this.type = this.getClass().getSimpleName();
	}

	public void setCard(CardSpell spell) {
		card = spell;
	}

	public CardSpell getCard() {
		return card;
	}

	public String getType() {
		return type;
	}
	
	public abstract void play();
	public abstract void play(IEntity e);
}
