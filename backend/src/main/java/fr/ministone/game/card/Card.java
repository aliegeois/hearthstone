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
	protected Long id;

	protected String deck;
	
	@Id
	protected String name;

	@Transient
	@JsonIgnore
	protected IPlayer owner;
	
	protected int manaCost;

	public Card() {}
	
	public Card(Long id, String deck, IPlayer owner, String name, int manaCost) {
		this.id = id;
		this.deck = deck;
		this.owner = owner;
		this.name = name;
		this.manaCost = manaCost;
	}
	
	public abstract void play();
	public abstract void play(IEntity target);

	public abstract Card copy();
	

	public Long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	
	public IPlayer getOwner() {
		return owner;
	}
	
	public int getManaCost() {
		return manaCost;
	}

	public void setId(Long newId) {
		this.id = newId;
	}
}