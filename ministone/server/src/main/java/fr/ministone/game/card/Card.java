package fr.ministone.game.card;

import fr.ministone.game.Player;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import fr.ministone.game.IEntity;

@MappedSuperclass
public abstract class Card {
	@Id
	protected String id;
	
	protected String name;

	@Transient
	protected Player owner;
	
	protected int manaCost;
	
	public Card(String id, Player owner, String name, int manaCost) {
		this.id = id;
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
	
	public Player getOwner() {
		return owner;
	}
	
	public int getManaCost() {
		return manaCost;
	}

	public void setId(String identif) {
		this.id = identif;
	}
}