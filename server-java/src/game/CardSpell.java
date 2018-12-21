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
	
	@Override
	public void play() {

		//S'il lui faut une target, on laisse l'utilisateur en choisir une
		if(singleEffects.isEmpty()) {
			//TODO
			//Entity target = 
		}

		for(SingleTargetEffect s : singleEffects) {
			s.play(target);	
		}
		
		for(MultipleTargetEffect m : multipleEffects) {
			m.play();
		}
		
		for(GlobalEffect g : globalEffects) {
			g.play();
		}
	}
}
