package fr.ministone.game.card;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import fr.ministone.game.IPlayer;
import fr.ministone.game.IEntity;

public class CardMinion extends Card implements IEntity {
	private int damageBase, damage, damageBoosted;
	private final int healthMax;
	private int health, healthBoosted;
	private Set<String> capacities;
	private Map<String, Integer> boosts;
	private boolean ready, provocation;
	
	public CardMinion(String id, IPlayer owner, String name, int mana, int damage, int health, Set<String> capacities, Map<String, Integer> boosts) {
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
		//Si le minion a été boosté alors qu'il était dans la main, on lui applique les boosts à son arrivée en jeu
		for(Map.Entry<String, Integer> boost : boosts.entrySet()) {
			for(CardMinion minion : owner.getBoard().values()) {
				if(!id.equals(minion.id)) {
					String boostName = boost.getKey();
					if("damage".equals(boostName)) {
						minion.buffDamage(boost.getValue());
					} else if("health".equals(boostName)) {
						minion.buffHealth(boost.getValue());
					}
				}
			}
		}
	}

	@Override
	public void play(IEntity target) {
		throw new UnsupportedOperationException();
	}
	
	
	public void attack(IEntity o) {
		o.takeDamage(damage);
		takeDamage(o.getDamage());	
	}
	
	@Override
	public void takeDamage(int quantity) {
		health -= quantity;
	}
	
	@Override
	public void heal(int quantity) {
		if(health + quantity <= healthMax) {
			health = health + quantity;
		} else {
			health = healthMax;
		}
	}
	
	@Override
	public void buffHealth(int quantity) {
		health += quantity;
		healthBoosted += quantity;
	}
	
	@Override
	public void buffDamage(int quantity) {
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
	
	@Override
	public int getHealth() {
		return health;
	}
	
	public int getHealthBoosted() {
		return healthBoosted;
	}
	
	public Set<String> getCapacities() {
		return capacities;
	}

	public Map<String,Integer> getBoosts() {
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
		return health <= 0;
	}
	
	@Override
	public void die() {
		owner.getBoard().remove(this.getId());
	}

	@Override
	public Card copy() {
		return new CardMinion(UUID.randomUUID().toString(), owner, name, manaCost, damage, health, capacities, boosts);
	}

	//@Override
	public void transform(IEntity e) {
		if(e instanceof CardMinion) {
			CardMinion into = (CardMinion)e;
			name = into.name;
			health = into.health;
			manaCost = into.manaCost;
			provocation = into.provocation;
			ready = into.ready; // Pas sûr qu'on le change
			damage = into.damage;
			// Si la carte donne des boosts on fait quoi ?
		} else {
			// TODO: ???
		}
	}
}
