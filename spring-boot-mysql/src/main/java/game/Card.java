package game;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;

@MappedSuperclass
abstract class Card {

	@Transient
	protected String id;

	@Id
	protected String name;

	//@JsonInclude()
	@Transient
	protected Player owner;

	protected int manaCost;
	
	Card(String id, Player owner, String name, int manaCost) {
		this.id = id;
		this.owner = owner;
		this.name = name;
		this.manaCost = manaCost;
	}
	
	public abstract void play();
	
	public String getName() {
		return name;
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public int getManaCost() {
		return manaCost;
	}

	public String getId(){
		return this.id;
	}

	public void setIdentifiant(String identif){
		this.id = identif;
	}

	public abstract Card copy();
}