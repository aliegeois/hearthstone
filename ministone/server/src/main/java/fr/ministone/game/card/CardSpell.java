package fr.ministone.game.card;

import java.util.Set;
import java.util.UUID;

import fr.ministone.game.effect.*;
import fr.ministone.game.IPlayer;
import fr.ministone.game.IEntity;

public class CardSpell extends Card {
	protected Set<SingleTargetEffect> singleEffects;
	protected Set<MultipleTargetEffect> multipleEffects;
	protected Set<GlobalEffect> globalEffects;
	
	public CardSpell(String id, IPlayer owner, String name, int mana, Set<SingleTargetEffect> single, Set<MultipleTargetEffect> multiple, Set<GlobalEffect> global) {
		super(id, owner, name, mana);
		this.singleEffects = single;
		this.multipleEffects = multiple;
		this.globalEffects = global;
	}
	
	@Override
	public void play() {
		for(MultipleTargetEffect m : multipleEffects) {
			m.play();
		}
		
		for(GlobalEffect g : globalEffects) {
			g.play();
		}
	}

	@Override
	public void play(IEntity e) {
		for(SingleTargetEffect s : singleEffects) {
			s.play(e);
		}

		for(MultipleTargetEffect m : multipleEffects) {
			m.play();
		}
		
		for(GlobalEffect g : globalEffects) {
			g.play();
		}
	}

	/*public void addEffect(SingleTargetEffect ste) {
		singleEffects.add(ste);
	}

	public void addEffect(MultipleTargetEffect mte) {
		multipleEffects.add(mte);
	}

	public void addEffect(GlobalEffect ge) {
		globalEffects.add(ge);
	}

	public Set<SingleTargetEffect> getSTE() {
		return singleEffects;
	}

	public Set<MultipleTargetEffect> getMTE() {
		return multipleEffects;
	}

	public Set<GlobalEffect> getGE() {
		return globalEffects;
	}*/

	@Override
	public Card copy() {
		return new CardSpell(UUID.randomUUID().toString(), owner, name, manaCost, singleEffects, multipleEffects, globalEffects);
	}
}
