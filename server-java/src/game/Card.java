package game;

class Card {
	protected final int id;
	protected String name;
	protected Player owner;
	protected int mana;
	
	Card(int id, Player owner, String name, int mana) {
		this.id = id;
		this.owner = owner;
		this.name = name;
		this.mana = mana;
	}
}