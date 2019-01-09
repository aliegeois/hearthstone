package game;

import java.util.Set;

import game.effect.*;

public class CardSpell extends Card {
	Set<SingleTargetEffect> singleEffects;
	Set<MultipleTargetEffect> multipleEffects;
	Set<GlobalEffect> globalEffects;
	
	public CardSpell(int id, Player owner, String name, int mana, Set<SingleTargetEffect> single, Set<MultipleTargetEffect> multiple, Set<GlobalEffect> global) {
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
			//s.play(target); TODO : gestion des targets	
		}
		
		for(MultipleTargetEffect m : multipleEffects) {
			m.play();
		}
		
		for(GlobalEffect g : globalEffects) {
			g.play();
		}
	}

	public void addEffect(SingleTargetEffect ste) {
		this.singleEffects.add(ste);
	}

	public void addEffect(MultipleTargetEffect mte) {
		this.multipleEffects.add(mte);
	}
	
	public void addEffect(GlobalEffect ge) {
		this.globalEffects.add(ge);
	}

}
