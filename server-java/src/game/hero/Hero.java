package game.hero;

import game.Constants;
import game.Entity;
import game.Player;

public abstract class Hero implements Entity {
	protected Player player;
	protected int health = Constants.HEROMAXHEALTH;
	protected int healthMax = Constants.HEROMAXHEALTH;
	protected int armor = 0;
	protected boolean provocation = false;
	
	public Hero(Player player) {
		this.player = player;
	}
	
	public void special(Entity e) {}
	
	public void takeDamage(int quantity) {
		if (armor > 0) {
			int tampon = quantity;
			quantity -= armor;
			armor -= tampon;
		}
		if (armor < 0) {
			armor = 0;
		}
		
		this.health -= quantity;
	}
	
	@Override
	public int getDamage() {
		return 0;
	}
	
	public void heal(int quantity) {
		if(health + quantity <= healthMax) {
			health = health + quantity;
		} else {
			health = healthMax;
		}
	}
	
	public void boostHealth(int quantity) {
		health += quantity;
	}
	
	public void boostArmor(int quantity) {
		armor += quantity;
	}
	
	public boolean isProvoking() {
		return provocation;
	}
	
	@Override
	public boolean isDead() {
		return (health <= 0);
	}
	
	@Override
	public void die() {
		if(isDead()) {
			
		}
	}
}
