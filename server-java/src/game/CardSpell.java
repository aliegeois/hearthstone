package game;

import java.util.Set;

import game.effect.*;

public class CardSpell extends Card {
	Set<SingleTargetEffect> singleEffects;
	Set<MultipleTargetEffect> multipleEffects;
	Set<GlobalEffect> globalEffects;
	
	CardSpell(int id, Player owner, String name, int mana, Set<Effect> effects) {
		super(id, owner, name, mana);
		this.effects = effects;
	}
	
	/*void cast(CardMinion target) {
		for(Effect e : effects) {
			
		}
	}*/
}
