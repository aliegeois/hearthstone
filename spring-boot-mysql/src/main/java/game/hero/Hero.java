package game.hero;

import game.Constants;
import game.Entite;
import game.Player;

public abstract class Hero implements Entite {
	protected Player player;
	protected int health = Constants.HEROMAXHEALTH;
	protected int healthMax = Constants.HEROMAXHEALTH;
	protected int armor = 0;
	protected boolean provocation = false;
	
	public Hero(Player player) {
		this.player = player;
		if(player.getHero() == null){
			player.setHero(this);
		}
	}
	
	public void special(Entite e) {}
	
	public void takeDamage(int quantity) {
		System.out.println("flag a");
		this.armor = this.armor - quantity;
		System.out.println("flag b");
		if(this.armor < 0) { //Si on a cassé toute l'armure
		System.out.println("flag c 1");
			this.health = this.health + this.armor;
			this.armor = 0;
		}
		System.out.println("flag d");
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
	
	public int getArmor() {
		return this.armor;
	}

	public int getHealth() {
		return this.health;
	}

	public Player getOwner(){
		return this.player;
	}
	
	@Override
	public void die() {
		if(isDead()) {
			//TODO : faire gagner l'adversaire
			System.out.println(this.player.getName() + " a gagn� !");
		}
	}
}
