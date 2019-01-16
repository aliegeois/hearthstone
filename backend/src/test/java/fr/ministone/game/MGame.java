package fr.ministone.game;

import java.util.HashMap;
import java.util.HashSet;

import fr.ministone.User;
import fr.ministone.game.card.CardMinion;
import fr.ministone.game.card.CardSpell;
import fr.ministone.repository.CardMinionRepository;
import fr.ministone.repository.CardSpellRepository;
import fr.ministone.repository.MCardMinionRepository;
import fr.ministone.repository.MCardSpellRepository;

public class MGame extends Game {
	public MGame(User u1, User u2, CardMinionRepository minionRepo, CardSpellRepository spellRepo) {
		super(0l, new MSimpMessagingTemplate(), u1, u2, minionRepo, spellRepo);

		playing = players.get(u1.getName());
	}

	/*public MGame(User u1, User u2) {
		CardMinionRepository minionRepo = new MCardMinionRepository();
		CardSpellRepository spellRepo = new MCardSpellRepository();

		CardMinion carte1 = new CardMinion("warrior", "nom-test-carte1", 1, 1, 1, new HashSet<>(), new HashMap<>());
		CardMinion carte3 = new CardMinion("warrior", "nom-test-carte3", 1, 1, 1, new HashSet<>(), new HashMap<>());

		CardSpell carte2 = new CardSpell("paladin", "nom-test-carte2", 1, new HashSet<>(), new HashSet<>(), new HashSet<>());
		CardSpell carte4 = new CardSpell("paladin", "nom-test-carte4", 1, new HashSet<>(), new HashSet<>(), new HashSet<>());

		minionRepo.save(carte1);
		minionRepo.save(carte3);
		spellRepo.save(carte2);
		spellRepo.save(carte4);

		super("", new MSimpMessagingTemplate(), u1, u2, minionRepo, spellRepo);

		this.id = "";
		this.template = new MSimpMessagingTemplate();
		this.cardMinionRepository = new MCardMinionRepository();
		this.cardSpellRepository = new MCardSpellRepository();

		IPlayer p1 = new PlayerMock(u1.getHeroType());
		IPlayer p2 = new PlayerMock(u2.getHeroType());
		p1.setOpponent(p2);

		this.players.put(u1.getName(), p1);
		this.players.put(u2.getName(), p2);

		CardMinion carte1 = new CardMinion("warrior", "nom-test-carte1", 1, 1, 1, new HashSet<>(), new HashMap<>());
		CardMinion carte3 = new CardMinion("warrior", "nom-test-carte3", 1, 1, 1, new HashSet<>(), new HashMap<>());

		CardSpell carte2 = new CardSpell("paladin", "nom-test-carte2", 1, new HashSet<>(), new HashSet<>(), new HashSet<>());
		CardSpell carte4 = new CardSpell("paladin", "nom-test-carte4", 1, new HashSet<>(), new HashSet<>(), new HashSet<>());

		cardMinionRepository.save(carte1);
		cardMinionRepository.save(carte3);
		cardSpellRepository.save(carte2);
		cardSpellRepository.save(carte4);

		playing = p1;
	}*/
}