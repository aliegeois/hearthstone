package fr.ministone.game.card;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.ministone.game.IPlayer;
import fr.ministone.game.IEntity;

@Entity
public class CardMinion extends Card implements IEntity {
	private final int damageBase;

	@Transient
	@JsonIgnore
	private int damage;

	@Transient
	@JsonIgnore
	private int damageBoosted;

	private final int healthMax;

	@Transient
	@JsonIgnore
	private int health;

	@Transient
	@JsonIgnore
	private int healthBoosted;

	private boolean charge;

	private boolean taunt;

	private boolean lifesteal;

	private int boostHealth;

	private int boostDamage;

	@Transient
	@JsonIgnore
	private boolean ready;

	public CardMinion() {
		super();
		damageBase = 0;
		healthMax = 0;
	}
	
	public CardMinion(String id, String deck, IPlayer owner, String name, int mana, int damage, int health, Set<String> capacities, Map<String, Integer> boosts) {
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

		System.out.println("CardMinion(health: " + health + ")");
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
		System.out.println("CardMinion.takeDamage(" + quantity + ", health avant: " + health + ")");
		health -= quantity;
		System.out.println("health après: " + health);
	}
	
	@Override
	public void heal(int quantity) {
		System.out.println("CardMinion.heal(" + quantity + "), health avant: " + health + ")");
		if(health + quantity <= healthMax) {
			health = health + quantity;
		} else {
			health = healthMax;
		}
		System.out.println("CardMinion.heal(" + quantity + "), health après: " + health + ")");
	}
	
	@Override
	public void buffHealth(int quantity) {
		System.out.println("CardMinion.buffHealth(" + quantity + "), health avant: " + health + ")");
		health += quantity;
		healthBoosted += quantity;
		System.out.println("CardMinion.buffHealth(" + quantity + "), health après: " + health + ")");
	}
	
	@Override
	public void buffDamage(int quantity) {
		damage += quantity;
		damageBoosted += quantity;
	}

	@Override
	public void buffArmor(int quantity) {
		throw new UnsupportedOperationException();
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
		return taunt;
	}

	public boolean getLifesteal() {
		return lifesteal;
	}

	public boolean getCharge() {
		return charge;
	}

	public int getBoostHealth() {
		return boostHealth;
	}

	public int getBoostDamage() {
		return boostDamage;
	}

	public boolean isReady() {
		return ready;
	}

	public boolean isProvoking() {
		return taunt;
	}
	
	@Override
	public boolean isDead() {
		return (health <= 0);
	}

	public void setReady(boolean ready){
		this.ready = ready;
	}
	
	@Override
	public void die() {
		owner.getBoard().remove(id);
	}

	@Override
	public Card copy() {
		return copy(null);
	}

	@Override
	public Card copy(IPlayer owner) {
		System.out.println("CardMinion.copy_health: " + healthMax);


		Set<String> capacities = new HashSet<>();
		if(taunt) {
			capacities.add("taunt");
		}
		if(charge) {
			capacities.add("charge");
		}
		if(lifesteal) {
			capacities.add("lifesteal");
		}

		Map<String, Integer> boosts = new HashMap<>();
		boosts.put("health", boostHealth);
		boosts.put("damage", boostDamage);

		return new CardMinion(UUID.randomUUID().toString(), deck, owner, name, manaCost, damage, healthMax, capacities, boosts);
	}

	@Override
	public void transform(CardMinion into) {
		System.out.println("CardMinion.transform(health avant: " + health + ")");
		name = into.name;
		health = into.health;
		manaCost = into.manaCost;
		taunt = into.taunt;
		charge = into.charge;
		lifesteal = into.lifesteal;
		ready = into.ready; // Pas sûr qu'on le change
		damage = into.damage;
		// Si la carte donne des boosts on fait quoi ?
		System.out.println("CardMinion.transform(health après: " + health + ")");
	}
}
