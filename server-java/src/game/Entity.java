package game;

public interface Entity {
	public void takeDamage(int quantity);
	public void heal(int quantity);
	public void boostHealth(int quantity);
	public boolean isDead();
}
