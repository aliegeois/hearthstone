package fr.ministone.game.hero;

import fr.ministone.game.Constants;
import fr.ministone.game.IEntity;
import fr.ministone.game.IPlayer;
import fr.ministone.game.card.CardMinion;

public abstract class Hero implements IEntity {
	protected IPlayer player;
	protected int health = Constants.HEROHEALTHMAX;
	protected int healthMax = Constants.HEROHEALTHMAX;
	protected int armor = 0;
	protected boolean alreadyUsed = false;
	
	public abstract void special(IEntity e);
	public abstract void special();
	
	@Override
	public void takeDamage(int quantity) {
		System.out.println("takeDamage: init");
		armor = armor - quantity;
		if(armor < 0) { //Si on a cassé toute l'armure
			System.out.println("armor < 0");
			health += armor;
			armor = 0;
		} else {
			System.out.println("armor >= 0");
		}
		System.out.println("takeDamage: end");
	}
	
	@Override
	public int getDamage() {
		return 0;
	}
	
	@Override
	public void heal(int quantity) {
		if(health + quantity <= healthMax) {
			health = health + quantity;
		} else {
			health = healthMax;
		}
	}
	
	@Override
	public int getHealth() {
		return health;
	}

	@Override
	public void buffDamage(int quantity) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void buffHealth(int quantity) {
		health += quantity;
	}
	
	@Override
	public void buffArmor(int quantity) {
		armor += quantity;
	}
	
	@Override
	public boolean isDead() {
		return health <= 0;
	}
	
	public int getArmor() {
		return armor;
	}

	@Override
	public void die() {
		if(isDead()) {
			//TODO : faire gagner l'adversaire
			System.out.println(player.getName() + " a perdu !");
			// Normalement, ne rien faire, l'adversaire d'occupe de dire qu'il a gagné, normalement
		}
	}

	@Override
	public void transform(CardMinion into) {
		throw new UnsupportedOperationException();
	}
	
	
	public void setPlayer(IPlayer p) {
		player = p;
	}

	public IPlayer getPlayer() {
		return player;
	}

	public boolean isUsed() {
		return alreadyUsed;
	}

	public void setUsed(boolean alreadyUsed) {
		this.alreadyUsed = alreadyUsed;
	}
}
