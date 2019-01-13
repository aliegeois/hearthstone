package fr.ministone.game.card;

import fr.ministone.game.Player;
import fr.ministone.game.IEntity;

public abstract class Card {
	protected String id;
	protected String name;
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