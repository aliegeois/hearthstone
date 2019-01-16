package fr.ministone.game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.core.AbstractMessageSendingTemplate;

import fr.ministone.User;
import fr.ministone.game.*;
import fr.ministone.game.card.*;
import fr.ministone.game.effect.GlobalEffect;
import fr.ministone.game.effect.MultipleTargetEffect;
import fr.ministone.game.effect.SingleTargetEffect;
import fr.ministone.game.hero.*;
import fr.ministone.repository.CardMinionRepository;
import fr.ministone.repository.CardSpellRepository;
import fr.ministone.repository.MCardMinionRepository;
import fr.ministone.repository.MCardSpellRepository;

public class GameTest {

    private IGame game;
    private User user1, user2;
    //private CardMinion carte1, carte2;
    //private CardSpell carte3;

    //private AbstractMessageSendingTemplate<String> template;

    private PlayerMock player2 = new PlayerMock("paladin");
    private PlayerMock player1 = new PlayerMock("warrior");
	
    @BeforeEach
    public void init() {

        user1 = new User("Pat", "E", "1", "warrior");
		user2 = new User("Bob", "F", "1", "paladin");
		
		CardMinionRepository minionRepo = new MCardMinionRepository();
		CardSpellRepository spellRepo = new MCardSpellRepository();

		CardMinion carte5 = new CardMinion("shared", "nom-test-carte5", 1, 1, 1, new HashSet<>(), new HashMap<>());
		CardMinion carte6 = new CardMinion("shared", "nom-test-carte6", 1, 1, 1, new HashSet<>(), new HashMap<>());

		CardMinion carte1 = new CardMinion("warrior", "nom-test-carte1", 1, 1, 1, new HashSet<>(), new HashMap<>());
		CardMinion carte3 = new CardMinion("warrior", "nom-test-carte3", 1, 1, 1, new HashSet<>(), new HashMap<>());

		CardSpell carte2 = new CardSpell("paladin", "nom-test-carte2", 1, new HashSet<>(), new HashSet<>(), new HashSet<>());
		CardSpell carte4 = new CardSpell("paladin", "nom-test-carte4", 1, new HashSet<>(), new HashSet<>(), new HashSet<>());

		minionRepo.save(carte1);
		minionRepo.save(carte3);
		minionRepo.save(carte5);
		minionRepo.save(carte6);
		spellRepo.save(carte2);
		spellRepo.save(carte4);

		System.out.println("Cartes dans minionRepo: " + minionRepo.count());
		System.out.println("Cartes dans spellRepo: " + spellRepo.count());

		game = new MGame(user1, user2, minionRepo, spellRepo);
		game.receiveConfirmStart("Pat");
		game.receiveConfirmStart("Bob");


    }

    @Test
    public void testPlayerSet() {
     
        assertNotEquals(game.getPlaying(), null);
        assertNotEquals(game.getPlaying().getOpponent(), null);

    }

    @Test
    public void testHand() {

        assertEquals(3, game.getPlaying().getHand().size());
        assertEquals(4, game.getPlaying().getOpponent().getHand().size());

    }

    @Test
    public void testContainsPlayer() {

        assertTrue(game.containsPlayer("E"));
        assertTrue(game.containsPlayer("F"));

    }

    @Test
    public void testCheck() {

        assertFalse(game.checkBoard());

        game.getPlayer("E").getHero().takeDamage(Constants.HEROHEALTHMAX);

        assertTrue(game.checkBoard());
        
    }



}