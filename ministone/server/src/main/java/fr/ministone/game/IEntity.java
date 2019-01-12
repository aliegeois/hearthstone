package fr.ministone.game;

public interface IEntity {
	public void takeDamage(int quantity);
	public int getDamage();

	public void heal(int quantity);
	public int getHealth();

	public void buffDamage(int quantity);
	public void buffHealth(int quantity);
	
	//public boolean isProvoking();

	public boolean isDead();
	public void die();

	public void transform(IEntity e);
}
