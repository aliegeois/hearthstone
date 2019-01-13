package fr.ministone.repository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import fr.ministone.game.card.CardMinion;
import fr.ministone.game.card.CardSpell;
import fr.ministone.game.effect.GlobalEffect;
import fr.ministone.game.effect.MultipleTargetEffect;
import fr.ministone.game.effect.SingleTargetEffect;

import java.util.HashMap;
import java.util.HashSet;

@Component

public class CardCommandLineRunner implements CommandLineRunner {

    private final CardMinionRepository minionRepository;
    private final CardSpellRepository spellRepository;

    public CardCommandLineRunner(CardMinionRepository minionRepository, CardSpellRepository spellRepository) {
        this.minionRepository = minionRepository;
        this.spellRepository = spellRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        CardMinion mesKouilles = new CardMinion("1", "shared", null, "Mon gros chibre", 1, 1, 1, new HashSet<String>(), new HashMap<String, Integer>());
        minionRepository.save(mesKouilles);
        CardSpell maBite = new CardSpell("2", "shared", null, "Mon gigantesque braquemart", 2, new HashSet<SingleTargetEffect>(), new HashSet<MultipleTargetEffect>(), new HashSet<GlobalEffect>());
        spellRepository.save(maBite);

        minionRepository.findAll().forEach(System.out::println);
        spellRepository.findAll().forEach(System.out::println);
    }

}