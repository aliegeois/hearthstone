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
    private CardMinion carte1, carte3;
    private CardSpell carte2, carte4;

    private AbstractMessageSendingTemplate<String> template;

    private PlayerMock player2 = new PlayerMock("paladin");
    private PlayerMock player1 = new PlayerMock("warrior");
	
    @BeforeEach
    public void init() {

        user1 = new User("Pat", "E", "1", "warrior");
        user2 = new User("Bob", "F", "1", "mage");

        carte2 = new CardSpell(2l, "paladin", player2, "nom-test-carte2", 1, new HashSet<SingleTargetEffect>(), new HashSet<MultipleTargetEffect>(), new HashSet<GlobalEffect>());
        carte4 = new CardSpell(4l, "paladin", player2, "nom-test-carte4", 1, new HashSet<SingleTargetEffect>(), new HashSet<MultipleTargetEffect>(), new HashSet<GlobalEffect>());
        carte1 = new CardMinion(1l, "warrior", player1, "nom-test-carte1", 1, 1, 1, new HashSet<String>(), new HashMap<String, Integer>());
	    carte3 = new CardMinion(3l, "warrior", player1, "nom-test-carte3", 1, 1, 1, new HashSet<String>(), new HashMap<String, Integer>());

        CardMinionRepository minionRepository = new MCardMinionRepository();
        CardSpellRepository spellRepository = new MCardSpellRepository();

        minionRepository.save(carte1);
        minionRepository.save(carte3);
        spellRepository.save(carte2);
        spellRepository.save(carte4);

        template = new MSimpMessagingTemplate();

        game = new GameMock(user1, user2, minionRepository, spellRepository);

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