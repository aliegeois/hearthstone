package fr.ministone.game.card;

import fr.ministone.game.IPlayer;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.ministone.game.IEntity;

@MappedSuperclass
public abstract class Card {
	@Transient
	@JsonIgnore
	protected String id;

	protected String deck;
	
	@Id
	protected String name;

	@Transient
	@JsonIgnore
	protected IPlayer owner;
	
	protected int manaCost;

	public Card() {}
	
	public Card(String id, String deck, IPlayer owner, String name, int manaCost) {
		this.id = id;
		this.deck = deck;
		this.owner = owner;
		this.name = name;
		this.manaCost = manaCost;
	}
	
	public abstract void play();
	public abstract void play(IEntity target);

	public abstract Card copy();
	public abstract Card copy(IPlayer owner);
	

	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	
	public IPlayer getOwner() {
		return owner;
	}

	public String getDeck() {
		return deck;
	}
	
	public int getManaCost() {
		return manaCost;
	}

	public void setId(String newId) {
		this.id = newId;
	}
}