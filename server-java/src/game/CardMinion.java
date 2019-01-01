package game;

import java.util.Map;
import java.util.Set;
import game.hero.Hero;

public class CardMinion extends Card implements Entity {
	private int damageBase, damage, damageBoosted;
	private int healthBase, health, healthBoosted;
	private Set<String> capacities;
	private Map<String, Integer> boosts;
	private boolean ready, provocation;
	
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
		this.provocation = capacities.contains("provocation");
	}
	
	@Override
	public void play() {
		//Si le minion a été boosté alors qu'il était dans la main, on lui applique les boosts à son arrivée en jeu
		for(Map.Entry<String, Integer> boost : boosts.entrySet()) {
			for(CardMinion minion : getOwner().getBoard().values()) {
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
	
	
	void attack(Entity o) {
		o.takeDamage(this.damage);
		this.takeDamage(o.getDamage());
		
		if(o.isDead()) {
			o.die();
		}
		if(this.isDead()) {
			this.die();
		}
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
	
	public boolean isProvoking() {
		return provocation;
	}
	
	@Override
	public boolean isDead() {
		return (health <= 0);
	}
	
	@Override
	public void die() {
		if(isDead()) {
			this.getOwner().getBoard().remove(this);
		}
	}
}
