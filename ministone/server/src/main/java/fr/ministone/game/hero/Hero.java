package fr.ministone.game.hero;

import fr.ministone.game.Constants;
import fr.ministone.game.IEntity;
import fr.ministone.game.Player;

public abstract class Hero implements IEntity {
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
	
	public abstract void special(IEntity e);
	public abstract void special();
	
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
	public void boostDamage(int quantity) {
		throw new UnsupportedOperationException();
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

	@Override
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
			System.out.println(this.player.getName() + " a gagné !");
		}
	}

	@Override
	public void transform(IEntity e){

	}
}
