package game;

public interface Entity {
	public void takeDamage(int quantity);
	public int getDamage();
	public void heal(int quantity);
	public int getHealth();
	public void boostHealth(int quantity);
	public void boostDamage(int quantity);
	public boolean isProvoking();
	public boolean isDead();
	public void die();
	public void transform(Entity e);
}
