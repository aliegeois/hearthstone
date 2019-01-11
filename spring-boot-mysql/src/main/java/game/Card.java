package game;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
abstract class Card {
	protected String id;

	@Id
	protected String name;

	protected Player owner;

	@Column
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