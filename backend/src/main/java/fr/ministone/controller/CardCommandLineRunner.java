package fr.ministone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import org.springframework.stereotype.Component;

import fr.ministone.game.card.*;
import fr.ministone.repository.CardMinionRepository;
import fr.ministone.repository.CardSpellRepository;
import fr.ministone.game.effect.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
//import java.util.stream.Stream;
import java.util.Set;

@Component
public class CardCommandLineRunner implements CommandLineRunner {
	@Autowired
	private final CardMinionRepository minionRepository;
	@Autowired
	private final CardSpellRepository spellRepository;

	@Autowired
    public CardCommandLineRunner(CardMinionRepository minionRepository, CardSpellRepository spellRepository) {
		this.minionRepository = minionRepository;
		this.spellRepository = spellRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
		Collection<CardMinion> minions = new ArrayList<>();
		Collection<CardSpell> spells = new ArrayList<>();

		minions.add(new CardMinion("shared", "Sanglier brocheroc", 1, 1, 1, new HashSet<>(), new HashMap<>()));

		Set<String> capacities02 = new HashSet<>();
		capacities02.add("provocation");
		minions.add(new CardMinion("shared", "Soldat du comté-de-l'or", 1, 1, 2, capacities02, new HashMap<>()));

		Set<String> capacities03 = new HashSet<>();
		capacities03.add("charge");
		minions.add(new CardMinion("shared", "Chevaucheur de loup", 3, 3, 1, capacities03, new HashMap<>()));

		Map<String, Integer> boosts04 = new HashMap<>();
		boosts04.put("damage", 1);
		minions.add(new CardMinion("shared", "Chef de raid", 3, 2, 2, new HashSet<>(), boosts04));

		minions.add(new CardMinion("shared", "Yéti noroit", 4, 4, 5, new HashSet<>(), new HashMap<>()));


		
		Set<String> capacities11 = new HashSet<>();
		capacities11.add("provocation");
		CardMinion invocation = new CardMinion("special", "Image miroir_token", 0, 0, 2, capacities11, new HashMap<>());
		Set<GlobalEffect> gte11 = new HashSet<>();
		SummonSpecific effect11 = new SummonSpecific(invocation.getName(), 2);
		minions.add(invocation);
		gte11.add(effect11);
		spells.add(new CardSpell("mage", "Image miroir", 1, new HashSet<>(), new HashSet<>(), gte11));
		
		Set<MultipleTargetEffect> mte12 = new HashSet<>();
		mte12.add(new MultipleTargetDamage(false, false, false, true, 1));
		spells.add(new CardSpell("mage", "Explosion des arcanes", 2, new HashSet<>(), mte12, new HashSet<>()));

		CardMinion mouton = new CardMinion("special", "Métamorphose_token", 0, 1, 1, new HashSet<>(), new HashMap<>());
		minions.add(mouton);
		Set<SingleTargetEffect> ste13 = new HashSet<>();
		ste13.add(new Transform(mouton.getName()));
		spells.add(new CardSpell("mage", "Métamorphose", 4, ste13, new HashSet<>(), new HashSet<>()));



		Set<String> capacities21 = new HashSet<>();
		capacities21.add("charge");
		capacities21.add("lifesteal");
		minions.add(new CardMinion("paladin", "Champion frisselame", 4, 3, 2, capacities21, new HashMap<>()));

		Set<SingleTargetEffect> ste22 = new HashSet<>();
		ste22.add(new SingleTargetDamageBuff(3));
		spells.add(new CardSpell("paladin", "Bénédiction de puissance", 1, ste22, new HashSet<>(), new HashSet<>()));

		Set<MultipleTargetEffect> mte23 = new HashSet<>();
		mte23.add(new MultipleTargetDamage(false, false, true, true, 2));
		spells.add(new CardSpell("paladin", "Consécration", 4, new HashSet<>(), mte23, new HashSet<>()));



		Set<MultipleTargetEffect> mte31 = new HashSet<>();
		mte31.add(new MultipleTargetDamage(true, true, true, true, 1));
		spellRepository.save(new CardSpell("warrior", "Tourbillon", 1, new HashSet<SingleTargetEffect>(), mte31, new HashSet<GlobalEffect>()));

		Set<String> capacities32 = new HashSet<>();
		capacities32.add("provocation");
		minions.add(new CardMinion("warrior", "Avocat commis d'office", 2, 0, 7, capacities32, new HashMap<>()));

		/*Set<SingleTargetEffect> ste33 = new HashSet<>();
		ste33.add
		spells.add(new CardSpell("warrior", "Maîtrise du blocage", 3, singleEffects, multipleEffects, globalEffects))*/



		minions.add(new CardMinion("special", "Recrue de la Main d'argent", 1, 1, 1, new HashSet<>(), new HashMap<>()));



		for(CardMinion m : minions)
			minionRepository.save(m);

		for(CardSpell s : spells)
			spellRepository.save(s);

		System.out.println("spells:");
		spellRepository.findAll().forEach(spell -> {
			System.out.println("Spell " + spell.getName());
			System.out.println("ge: ");
			for(GlobalEffect ge : spell.getGE())
				System.out.println("Class: " + ge.getType());
			System.out.println("mte: ");
			for(MultipleTargetEffect mte : spell.getMTE())
				System.out.println("Class: " + mte.getType());
			System.out.println("ste: ");
			for(SingleTargetEffect ste : spell.getSTE())
				System.out.println("Class: " + ste.getType());
		});
    }
}