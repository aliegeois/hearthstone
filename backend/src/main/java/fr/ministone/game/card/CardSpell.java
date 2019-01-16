package fr.ministone.game.card;

import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import fr.ministone.game.effect.*;
import fr.ministone.game.IPlayer;
import fr.ministone.game.IEntity;

// Essayer ça: https://stackoverflow.com/questions/3774198/org-hibernate-mappingexception-could-not-determine-type-for-java-util-list-at

@Entity
public class CardSpell extends Card {
	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
	protected Set<SingleTargetEffect> singleEffects;

	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
	protected Set<MultipleTargetEffect> multipleEffects;

	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
	protected Set<GlobalEffect> globalEffects;

	public CardSpell() {
		super();
	}
	
	public CardSpell(String id, String deck, IPlayer owner, String name, int mana, Set<SingleTargetEffect> singleEffects, Set<MultipleTargetEffect> multipleEffects, Set<GlobalEffect> globalEffects) {
		super(id, deck, owner, name, mana);
		this.singleEffects = singleEffects;
		this.multipleEffects = multipleEffects;
		this.globalEffects = globalEffects;
		
		for(SingleTargetEffect e : this.singleEffects)
			e.setCard(this);
		for(MultipleTargetEffect e : this.multipleEffects)
			e.setCard(this);
		for(GlobalEffect e : this.globalEffects)
			e.setCard(this);
	}

	public CardSpell(String deck, String name, int mana, Set<SingleTargetEffect> singleEffects, Set<MultipleTargetEffect> multipleEffects, Set<GlobalEffect> globalEffects) {
		this(null, deck, null, name, mana, singleEffects, multipleEffects, globalEffects);
	}
	
	@Override
	public void play() {
		System.out.println("CardSpell.play()");
		for(MultipleTargetEffect m : multipleEffects) {
			m.play();
		}
		
		for(GlobalEffect g : globalEffects) {
			g.play();
		}
	}

	@Override
	public void play(IEntity e) {
		System.out.println("CardSpell.play(" + e + ")");
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

	public Set<SingleTargetEffect> getSTE() {
		return singleEffects;
	}

	public Set<MultipleTargetEffect> getMTE() {
		return multipleEffects;
	}

	public Set<GlobalEffect> getGE() {
		return globalEffects;
	}

	@Override
	public Card copy() {
		return new CardSpell(UUID.randomUUID().toString(), deck, owner, name, manaCost, singleEffects, multipleEffects, globalEffects);
	}
}
