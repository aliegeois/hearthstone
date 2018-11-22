package game.hero;

import game.Constants;
import game.Entity;
import game.Player;

public abstract class Hero implements Entity {
	protected Player player;
	protected int health = Constants.heroMaxHealth;
	protected int healthMax = Constants.heroMaxHealth;
	
	public Hero(Player player) {
		this.player = player;
	}
	
	public void special(Entity e) {}
	
	public void takeDamage(int quantity) {
		//health -= c
	}
}
