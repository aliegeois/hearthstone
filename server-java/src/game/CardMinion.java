package game;

public class CardMinion extends Card implements Entity {
	private int damageBase, damage, damageBoosted;
	private int healthBase, health, healthBoosted;
	
	
	CardMinion(int id, Player player, String name, int mana, int damage, int health) {
		super(id, player, name, mana);
		this.damageBase = damage;
		this.damage = damage;
		this.damageBoosted = 0;
		this.healthBase = health;
		this.health = health;
		this.healthBoosted = 0;
	}
	
	void summon() {
		
	}
	
	void attackMinion(CardMinion o) {
		o.takeDamage(this);
		takeDamage(o);
	}
	
	public void takeDamage(Entity e) {
		
	}
}
