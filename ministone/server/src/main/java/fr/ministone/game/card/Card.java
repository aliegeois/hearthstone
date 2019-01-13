package fr.ministone.game.card;

import fr.ministone.game.IPlayer;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import fr.ministone.game.IEntity;

@MappedSuperclass
public abstract class Card {
	@Id
	protected String id;

	protected String deck;
	
	protected String name;

	@Transient
	protected IPlayer owner;
	
	protected int manaCost;
	
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
	

	public String getId() {
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

	public void setId(String identif) {
		this.id = identif;
	}
}