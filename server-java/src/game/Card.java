package game;

abstract class Card {
	protected final int id;
	protected String name;
	protected Player owner;
	protected int manaCost;
	
	Card(int id, Player owner, String name, int manaCost) {
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

	public int getId(){
		return this.id;
	}
}