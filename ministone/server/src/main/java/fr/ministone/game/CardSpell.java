package fr.ministone.game;

import java.util.Set;
import java.util.UUID;

import fr.ministone.game.effect.*;

public class CardSpell extends Card {
	protected Set<SingleTargetEffect> singleEffects;
	protected Set<MultipleTargetEffect> multipleEffects;
	protected Set<GlobalEffect> globalEffects;
	
	public CardSpell(String id, Player owner, String name, int mana, Set<SingleTargetEffect> single, Set<MultipleTargetEffect> multiple, Set<GlobalEffect> global) {
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

	public void addEffect(SingleTargetEffect ste) {
		this.singleEffects.add(ste);
	}

	public void addEffect(MultipleTargetEffect mte) {
		this.multipleEffects.add(mte);
	}
	
	public void addEffect(GlobalEffect ge) {
		this.globalEffects.add(ge);
	}

	public Set<SingleTargetEffect> getSTE(){
		return this.singleEffects;
	}

	public Set<MultipleTargetEffect> getMTE(){
		return this.multipleEffects;
	}

	public Set<GlobalEffect> getGE(){
		return this.globalEffects;
	}

	@Override
	public Card copy(){
		
		String identif = UUID.randomUUID().toString();
		
		Card carte = new CardSpell(identif, this.getOwner(), this.getName(), this.getManaCost(), this.getSTE(), this.getMTE(), this.getGE());
		return carte;
	}

}
