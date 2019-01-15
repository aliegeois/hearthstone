package fr.ministone.game.card;

import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
//import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import fr.ministone.game.effect.*;
import fr.ministone.game.IPlayer;
import fr.ministone.game.IEntity;

// Essayer ça: https://stackoverflow.com/questions/3774198/org-hibernate-mappingexception-could-not-determine-type-for-java-util-list-at

@Entity
public class CardSpell extends Card {
	//@OneToMany(fetch = FetchType.EAGER, /*mappedBy = "id", */cascade = {CascadeType.ALL})
	@OneToMany
	//@JoinColumn(name = "id")
	//@ElementCollection
	protected Set<SingleTargetEffect> singleEffects;

	//@OneToMany(fetch = FetchType.EAGER, /*mappedBy = "id", */cascade = {CascadeType.ALL})
	@OneToMany
	//@JoinColumn(name = "id")
	//@ElementCollection
	protected Set<MultipleTargetEffect> multipleEffects;

	//@OneToMany(fetch = FetchType.EAGER, /*mappedBy = "id", */cascade = {CascadeType.ALL})
	@OneToMany
	//@JoinColumn(name = "id")
	//@ElementCollection
	protected Set<GlobalEffect> globalEffects;

	public CardSpell() {
		super();
	}
	
	public CardSpell(Long id, String deck, IPlayer owner, String name, int mana, Set<SingleTargetEffect> singleEffects, Set<MultipleTargetEffect> multipleEffects, Set<GlobalEffect> globalEffects) {
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
	}*/

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
		return new CardSpell(UUID.randomUUID().getLeastSignificantBits(), deck, owner, name, manaCost, singleEffects, multipleEffects, globalEffects);
	}
}
