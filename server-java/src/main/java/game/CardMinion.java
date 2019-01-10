package main.java.game;

import java.nio.file.CopyOption;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CardMinion extends Card implements Entity {
	private int damageBase, damage, damageBoosted;
	private final int healthMax;
	private int health, healthBoosted;
	private Set<String> capacities;
	private Map<String, Integer> boosts;
	private boolean ready, provocation;
	
	public CardMinion(String id, Player owner, String name, int mana, int damage, int health, Set<String> capacities, Map<String, Integer> boosts) {
		super(id, owner, name, mana);
		this.damageBase = damage;
		this.damage = damage;
		this.damageBoosted = 0;
		this.healthMax = health;
		this.health = health;
		this.healthBoosted = 0;
		this.capacities = capacities;
		this.boosts = boosts;		
		this.ready = capacities.contains("charge");
		this.provocation = capacities.contains("provocation");
	}
	
	@Override
	public void play() {
		//Si le minion a �t� boost� alors qu'il �tait dans la main, on lui applique les boosts � son arriv�e en jeu
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
	
	
	public void attack(Entity o) {
		o.takeDamage(this.damage);
		this.takeDamage(o.getDamage());		
	}
	
	public void takeDamage(int quantity) {
		health -= quantity;
		if(this.isDead()) {
			this.die();
		}
	}
	
	public void heal(int quantity) {
		if(health + quantity < healthMax) {
			health = health + quantity;
		} else {
			health = healthMax;
		}
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
	
	public int getHealthMax() {
		return healthMax;
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

	public Map<String,Integer> getBoosts(){
		return boosts;
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
		this.getOwner().getBoard().remove(this.getId());
	}

	@Override
	public Card copy(){

		String identif = UUID.randomUUID().toString();
		Player player = this.getOwner();
		String name = this.getName();
		int mana = this.getManaCost();
		int damage = this.getDamageBase();
		int health = this.getHealth();
		Set<String> effects = this.getEffects();
		Map<String, Integer> boosts = this.getBoosts();

		Card carte = new CardMinion(identif, player, name, mana, damage, health, effects, boosts);
		return carte;
	}
}
