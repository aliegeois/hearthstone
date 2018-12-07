package game;

import java.util.Set;

import game.effect.*;

public class CardSpell extends Card {
	Set<SingleTargetEffect> singleEffects;
	Set<MultipleTargetEffect> multipleEffects;
	Set<GlobalEffect> globalEffects;
	
	CardSpell(int id, Player owner, String name, int mana, Set<SingleTargetEffect> single, Set<MultipleTargetEffect> multiple, Set<GlobalEffect> global) {
		super(id, owner, name, mana);
		this.singleEffects = single;
		this.multipleEffects = multiple;
		this.globalEffects = global;
	}
	
	void cast(CardMinion target) {
		for(SingleTargetEffect s : singleEffects) {
			
		}
		
		for(MultipleTargetEffect m : multipleEffects) {
			
		}
		
		for(GlobalEffect g : globalEffects) {
			
		}
	}
}
