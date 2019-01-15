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
import fr.ministone.game.hero.*;

public class GameTest {

    private IGame game;
    private User user1, user2;
    private CardMinion carte1, carte2;
    private CardSpell carte3;

    private AbstractMessageSendingTemplate<String> template;


    @BeforeEach
    public void init() {

        user1 = new User("Pat", "E", "1", "warrior");
        user2 = new User("Bob", "F", "1", "mage");

        template = new MSimpMessagingTemplate();

        game = new Game("game", template, user1, user2);

    }

    @Test
    public void testPlayerSet() {
     
        assertFalse(game.getPlaying() == null);
        assertFalse(game.getPlaying().getOpponent() == null);

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