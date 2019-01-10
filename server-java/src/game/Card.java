package game;

abstract class Card {
	protected String id;
	protected String name;
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