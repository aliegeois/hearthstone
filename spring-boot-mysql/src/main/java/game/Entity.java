package game;

public interface Entity {
	public void takeDamage(int quantity);
	public int getDamage();
	public void heal(int quantity);
	public void boostHealth(int quantity);
	public boolean isProvoking();
	public boolean isDead();
	public void die();
}
