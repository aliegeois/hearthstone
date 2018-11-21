package game;

abstract class Hero implements Entity {
	protected Player player;
	protected int health = Constants.heroMaxHealth;
	protected int healthMax = Constants.heroMaxHealth;
	
	Hero(Player player) {
		this.player = player;
	}
	
	void special(Entity e) {}
	
	public void takeDamage(Entity e) {
		//health -= c
	}
}
