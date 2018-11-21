package game;

import java.util.Map;
import java.util.Set;

public class CardMinion extends Card implements Entity {
	int damageBase, damage, damageBoosted;
	int healthBase, health, healthBoosted;
	private Set<String> effects;
	private Map<String, Integer> boosts;
	boolean ready;
	
	CardMinion(int id, Player owner, String name, int mana, int damage, int health, Set<String> effects, Map<String, Integer> boosts) {
		super(id, owner, name, mana);
		this.damageBase = damage;
		this.damage = damage;
		this.damageBoosted = 0;
		this.healthBase = health;
		this.health = health;
		this.healthBoosted = 0;
		this.effects = effects;
		this.boosts = boosts;		
		this.ready = effects.contains("charge");
	}
	
	void summon() {
		for(Map.Entry<String, Integer> boost : boosts.entrySet()) {
			for(CardMinion minion : owner.board.values()) {
				if(minion.id != id) {
					switch(boost.getKey()) {
					case "damage":
						minion.damage += boost.getValue();
						minion.damageBoosted += boost.getValue();
						break;
					case "health":
						minion.health += boost.getValue();
						minion.healthBoosted += boost.getValue();
					}
				}
			}
		}
	}
	
	void attackMinion(CardMinion o) {
		o.takeDamage(this);
		takeDamage(o);
		if(o.health <= 0)
			owner.opponent.board.remove(o.id);
		if(health <= 0)
			owner.board.remove(id);
	}
	
	public void takeDamage(Entity e) {
		
	}
}
