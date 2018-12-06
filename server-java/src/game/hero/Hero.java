package game.hero;

import game.Constants;
import game.Entity;
import game.Player;

public abstract class Hero implements Entity {
	protected Player player;
	protected int health = Constants.HEROMAXHEALTH;
	protected int healthMax = Constants.HEROMAXHEALTH;
	
	public Hero(Player player) {
		this.player = player;
	}
	
	public void special(Entity e) {}
	
	public void takeDamage(int quantity) {
		//health -= c
	}
	
	public void heal(int quantity) {
		if(health + quantity <= healthMax)
			health = health + quantity;
	}
	
	public void boostHealth(int quantity) {
		health += quantity;
	}
}
