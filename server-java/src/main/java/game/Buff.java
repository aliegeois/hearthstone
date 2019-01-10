package main.java.game;

public class Buff {
	private int health;
	private int damage;
	
	public Buff(int health, int damage) {
		this.health = health;
		this.damage = damage;
	}
	
	public int getHealth() {
		return health;
	}
	
	public int getDamage() {
		return damage;
	}
}
