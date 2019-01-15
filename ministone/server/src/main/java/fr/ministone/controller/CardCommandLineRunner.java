package fr.ministone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import org.springframework.stereotype.Component;

import fr.ministone.game.card.CardMinion;
import fr.ministone.game.card.CardSpell;
import fr.ministone.repository.CardMinionRepository;

import java.util.HashMap;
import java.util.HashSet;
//import java.util.stream.Stream;

@Component

public class CardCommandLineRunner<SpellMinionRepository> implements CommandLineRunner {
	@Autowired
	private final CardMinionRepository minionRepository;
	@Autowired
	private final SpellMinionRepository spellRepository;

	@Autowired
    public CardCommandLineRunner(CardMinionRepository minionRepository, SpellMinionRepository spellRepository) {
		this.minionRepository = minionRepository;
		this.spellRepository = spellRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
		//CardMinion sanglier = new CardMinion("1", "shared", null, "Sanglier brocheroc", 1, 1, 1, new HashSet<String>(), new HashMap<String, Integer>());
		//minionRepository.save(sanglier);

		//Set<SingleTargetEffect> ste = new HashSet<>();
		//CardSpell imageMiroir = new CardSpell("2", "mage", null, "Image miroir", 1, singleEffects, multipleEffects, globalEffects);

        /*Stream.of("Nero", "Claudius", "Dorō, monsutā cādo !").forEach(name ->
                repository.save(new User(name))
        );

        repository.findAll().forEach(System.out::println);*/
    }

}