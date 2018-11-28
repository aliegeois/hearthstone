package game;

import java.util.Map;
import java.util.Set;

public class CardMinion extends Card implements Entity {
	private int damageBase, damage, damageBoosted;
	private int healthBase, health, healthBoosted;
	private Set<String> capacities;
	private Map<String, Integer> boosts;
	private boolean ready;
	
	public CardMinion(int id, Player owner, String name, int mana, int damage, int health, Set<String> capacities, Map<String, Integer> boosts) {
		super(id, owner, name, mana);
		this.damageBase = damage;
		this.damage = damage;
		this.damageBoosted = 0;
		this.healthBase = health;
		this.health = health;
		this.healthBoosted = 0;
		this.capacities = capacities;
		this.boosts = boosts;		
		this.ready = capacities.contains("charge");
	}
	
	public void summon() {
		for(Map.Entry<String, Integer> boost : boosts.entrySet()) {
			for(CardMinion minion : owner.getBoard().values()) {
				if(minion.id != id) {
					switch(boost.getKey()) {
					case "damage":
						//minion.damage += boost.getValue();
						//minion.damageBoosted += boost.getValue();
						boostDamage(boost.getValue());
						break;
					case "health":
						//minion.health += boost.getValue();
						//minion.healthBoosted += boost.getValue();
						boostHealth(boost.getValue());
					}
				}
			}
		}
	}
	
	void attackMinion(CardMinion o) {
		o.takeDamage(damage);
		takeDamage(o.damage);
		if(o.health <= 0)
			owner.getOpponent().getBoard().remove(o.id);
		if(health <= 0)
			owner.getBoard().remove(id);
	}
	
	public void takeDamage(int quantity) {
		health -= quantity;
	}
	
	public void heal(int quantity) {
		if(health + quantity <= healthBase)
			health = health + quantity;
	}
	
	public void boostHealth(int quantity) {
		health += quantity;
		healthBoosted += quantity;
	}
	
	public void boostDamage(int quantity) {
		damage += quantity;
		damageBoosted += quantity;
	}
	
	public int getDamageBase() {
		return damageBase;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public int getDamageBoosted() {
		return damageBoosted;
	}
	
	public int getHealthBase() {
		return healthBase;
	}
	
	public int getHealth() {
		return health;
	}
	
	public int getHealthBoosted() {
		return healthBoosted;
	}
	
	public Set<String> getEffects() {
		return capacities;
	}
	
	public boolean isReady() {
		return ready;
	}
}
