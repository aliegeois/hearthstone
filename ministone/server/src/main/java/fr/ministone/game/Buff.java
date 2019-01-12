package fr.ministone.game;

public class Buff {
	private String sourceId;
	private int health;
	private int damage;
	
	public Buff(String sourceId, int health, int damage) {
		this.sourceId = sourceId;
		this.health = health;
		this.damage = damage;
	}

	public String getSourceId() {
		return sourceId;
	}
	
	public int getHealth() {
		return health;
	}
	
	public int getDamage() {
		return damage;
	}
}
