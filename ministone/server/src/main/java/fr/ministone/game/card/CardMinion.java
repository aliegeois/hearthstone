package fr.ministone.game.card;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Transient;

import fr.ministone.game.IPlayer;
import fr.ministone.game.IEntity;

@Entity
public class CardMinion extends Card implements IEntity {
	private int damageBase;

	@Transient
	private int damage;

	@Transient
	private int damageBoosted;

	private final int healthMax;

	@Transient
	private int health;

	@Transient
	private int healthBoosted;

	private boolean charge;

	private boolean taunt;

	private boolean lifesteal;

	private int boostHealth;

	private int boostDamage;

	@Transient
	private boolean ready;

	public CardMinion() {
		super();
		healthMax = 0;
	}
	
	public CardMinion(Long id, String deck, IPlayer owner, String name, int mana, int damage, int health, Set<String> capacities, Map<String, Integer> boosts) {
		super(id, deck, owner, name, mana);
		this.damageBase = damage;
		this.damage = damage;
		this.damageBoosted = 0;
		this.healthMax = health;
		this.health = health;
		this.healthBoosted = 0;

		this.charge = capacities.contains("charge");
		this.taunt = capacities.contains("provocation");
		this.lifesteal = capacities.contains("lifesteal");
		
		this.boostHealth = boosts.containsKey("health") ? boosts.get("health") : 0;
		this.boostDamage = boosts.containsKey("damage") ? boosts.get("damage") : 0;

		this.ready = this.charge;
	}

	public CardMinion(String deck, String name, int mana, int damage, int health, Set<String> capacities, Map<String, Integer> boosts) {
		this(null, deck, null, name, mana, damage, health, capacities, boosts);
	}
	
	@Override
	public void play() {
		// TODO : modifer pour faire en sorte que le bost ne soit actif que tant que le minion est en vie (attribut boost dans chaque carte avec référence vers la classe qui buff, et trigger lors de la méthode die())
		//Si le minion a été boosté alors qu'il était dans la main, on lui applique les boosts à son arrivée en jeu

		for(CardMinion minion : owner.getBoard().values()) {
			if(!id.equals(minion.id)) {
				minion.buffDamage(this.boostDamage);
				minion.buffHealth(this.boostHealth);
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
	
	public boolean getTaunt() {
		return this.taunt;
	}

	public boolean getLifesteal() {
		return this.lifesteal;
	}

	public boolean getCharge() {
		return this.charge;
	}

	public int getBoostHealth() {
		return this.boostHealth;
	}

	public int getBoostDamage() {
		return this.boostDamage;
	}

	public boolean isReady() {
		return ready;
	}

	public boolean isProvoking() {
		return this.taunt;
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
		Set<String> capacities = new HashSet<String>();
		if(this.taunt) {
			capacities.add("taunt");
		}
		if(this.charge) {
			capacities.add("add");
		}
		if(this.lifesteal) {
			capacities.add("lifesteal");
		}

		Map<String, Integer> boosts = new HashMap<String, Integer>();
		boosts.put("health", this.boostHealth);
		boosts.put("damage", this.boostDamage);

		return new CardMinion(UUID.randomUUID().getLeastSignificantBits(), deck, owner, name, manaCost, damage, health, capacities, boosts);
	}

	@Override
	public void transform(CardMinion into) {
		name = into.name;
		health = into.health;
		manaCost = into.manaCost;
		taunt = into.taunt;
		charge = into.charge;
		lifesteal = into.lifesteal;
		ready = into.ready; // Pas sûr qu'on le change
		damage = into.damage;
		// Si la carte donne des boosts on fait quoi ?
	}
}
