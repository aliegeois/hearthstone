package fr.ministone.game.effect;

//import fr.ministone.game.card.CardMinion;
import fr.ministone.game.card.CardSpell;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.ministone.game.IEntity;

@MappedSuperclass
public abstract class Effect {
	@Id
	@GeneratedValue
	private Long id;

	@Transient
	@JsonIgnore
	protected CardSpell card;

	@JsonProperty
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
	/*public abstract void play(CardMinion minion);
	public abstract void play(CardSpell spell);*/ // Snif mon idée géniale...
}
