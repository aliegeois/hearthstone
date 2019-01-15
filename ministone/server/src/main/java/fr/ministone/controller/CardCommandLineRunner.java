package fr.ministone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import org.springframework.stereotype.Component;

import fr.ministone.game.card.*;
import fr.ministone.repository.CardMinionRepository;
import fr.ministone.repository.CardSpellRepository;
import fr.ministone.game.effect.*;

import java.util.HashMap;
import java.util.HashSet;
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
		CardMinion sanglier = new CardMinion(1l, "shared", null, "Sanglier brocheroc", 1, 1, 1, new HashSet<String>(), new HashMap<String, Integer>());
		minionRepository.save(sanglier);

		Set<String> capacities3 = new HashSet<>();
		capacities3.add("provocation");
		CardMinion invocation = new CardMinion(3l, "mage", null, "Image miroir_token", 0, 2, 0, capacities3, new HashMap<String, Integer>());
		minionRepository.save(invocation);
		Set<GlobalEffect> gte2 = new HashSet<>();
		SummonSpecific effect31 = new SummonSpecific(invocation.getName());
		
		gte2.add(effect31);
		CardSpell imageMiroir = new CardSpell(2l, "mage", null, "Image miroir", 1, new HashSet<SingleTargetEffect>(), new HashSet<MultipleTargetEffect>(), gte2);
		spellRepository.save(imageMiroir);

		Set<String> capacities4 = new HashSet<>();
		capacities4.add("charge");
		capacities4.add("lifesteal");
		CardMinion championFrisselame = new CardMinion(4l, "paladin", null, "Champion frisselame", 4, 3, 2, capacities4, new HashMap<String, Integer>());
		minionRepository.save(championFrisselame);

		Set<MultipleTargetEffect> mte5 = new HashSet<>();
		mte5.add(new MultipleTargetDamage(true, true, false, false, 1));
		CardSpell tourbillon = new CardSpell(5l, "warrior", null, "Tourbillon", 1, new HashSet<SingleTargetEffect>(), mte5, new HashSet<GlobalEffect>());
		spellRepository.save(tourbillon);

        /*Stream.of("Nero", "Claudius", "Dorō, monsutā cādo !").forEach(name ->
                repository.save(new User(name))
        );*/

		/*System.out.println("spells:");
		spellRepository.findAll().forEach(spell -> {
			System.out.println("Spell " + spell.getName());
			System.out.println("ge: ");
			for(GlobalEffect ge : spell.getGE())
				System.out.println("Class: " + ge.getClass().getName());
			System.out.println("mte: ");
			for(MultipleTargetEffect mte : spell.getMTE())
				System.out.println("Class: " + mte.getClass().getName());
			System.out.println("ste: ");
			for(SingleTargetEffect ste : spell.getSTE())
				System.out.println("Class: " + ste.getClass().getName());
		});*/
    }
}