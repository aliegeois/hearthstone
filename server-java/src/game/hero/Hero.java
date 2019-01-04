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
		this.armor = this.armor - quantity;
		if(this.armor < 0) { //Si on a cassé toute l'armure
			this.health = this.health + this.armor;
			this.armor = 0;
		}
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
			//TODO : faire gagner l'adversaire
			System.out.println(this.player.getName() + " a gagn� !");
		}
	}
}
