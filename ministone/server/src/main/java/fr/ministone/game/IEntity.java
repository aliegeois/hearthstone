package fr.ministone.game;

import fr.ministone.game.card.CardMinion;

public interface IEntity {
	public void takeDamage(int quantity);
	public int getDamage();

	public void heal(int quantity);
	public int getHealth();

	public void buffDamage(int quantity);
	public void buffHealth(int quantity);

	public boolean isDead();
	public void die();

	public void transform(CardMinion into);
}
