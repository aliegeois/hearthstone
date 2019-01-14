package fr.ministone.game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ministone.game.card.Card;
import fr.ministone.game.card.CardMinion;
import fr.ministone.game.card.CardSpell;
import fr.ministone.game.Constants;
import fr.ministone.game.IPlayer;
import fr.ministone.game.effect.*;

public class PlayerTest {

    private IPlayer player1 = new Player("Billy", "E", "warrior");
    private IPlayer player2 = new Player("Bob", "F", "mage");
    private IPlayer player3;
    private IPlayer player4;
    private Set<String> cap1 = new HashSet<String>();
    private Set<String> cap2 = new HashSet<String>();
    
    private CardMinion carte1, carte2, carte4;
    private CardSpell carte3;


    @BeforeEach
    public void init() {
        player3 = new MPlayer("paladin");
        player4 = new MPlayer("mage");
        player3.setOpponent(player4);

        carte4 = new CardMinion("4", "paladin", player3, "Carte 4", 1, 1, 1, new HashSet<String>(), new HashMap<String, Integer>());
        player3.getDeck().add(carte4);


        player1.setOpponent(player2);
        this.cap1.add("charge");
        this.cap2.add("provocation");
        this.carte1 = new CardMinion("1", "shared", player1, "carte1", 1, 1, 10, cap1, new HashMap<String,Integer>());
        this.carte2 = new CardMinion("2", "shared", player2, "carte2", 1, 1, 10, cap2, new HashMap<String,Integer>());
        this.carte3 = new CardSpell("3", "shared", player1, "carte3", 1, new HashSet<SingleTargetEffect>(), new HashSet<MultipleTargetEffect>(), new HashSet<GlobalEffect>());

        this.player1.getDeck().add(carte1);
        this.player2.getDeck().add(carte2);

        this.player1.getBoard().put("1", carte1);
        this.player2.getBoard().put("2", carte2);

    }

    @Test 
    public void testInit() {
        assertNotEquals(null, player1.getHero());
        assertNotEquals(null, player2.getHero());
    }
    @Test
    public void testDrawCard() {
        assertEquals(0, player1.getHand().size());
        player1.drawCard(false);
        assertEquals(1, player1.getHand().size());
    }

    @Test
    public void testPlayMinion() {
        /*player1.nextTurn();
        Card cardDrawn = player1.drawCard(false);
        assertEquals(1, player1.getHand().size());
        assertEquals(1, player1.getBoard().size());

        player1.summonMinion(cardDrawn.getId());
        assertEquals(0, player1.getHand().size());
        assertEquals(2, player1.getBoard().size());*/

        Card cardDrawn = player3.drawCard(false);
        assertEquals(1, player3.getHand().size());
        assertEquals(0, player3.getBoard().size());

        player3.summonMinion(cardDrawn.getId());
        assertEquals(0, player3.getHand().size());
        assertEquals(1, player3.getBoard().size());
    }

    @Test
    public void testPlaySpell() {
        assertEquals(0, player1.getHand().size());
        player1.getHand().put(carte3.getId(), carte3);
        assertEquals(1, player1.getHand().size());

        player1.castSpell(carte3.getId());

        assertEquals(0, player1.getHand().size());
    }

    @Test
    public void testHeroSpecial() {
        assertEquals(0, player1.getHero().getArmor());
        player1.heroSpecial();
        assertEquals(2, player1.getHero().getArmor());

        player2.heroSpecial(false, "hero");
        assertEquals(0, player1.getHero().getArmor());
        assertEquals(Constants.HEROHEALTHMAX, player1.getHero().getHealth());
    }

    @Test
    public void testSetOpponent() {
        player1.setOpponent(player2);

        assertEquals(player2, player1.getOpponent());
        assertEquals(player1, player2.getOpponent());
    }

    @Test 
    public void testAttackMinion() {
        assertEquals(10, carte1.getHealth());
        assertEquals(10, carte2.getHealth());
        player1.attack(carte1.getId(), carte2.getId());

        assertEquals(9, carte1.getHealth());
        assertEquals(9, carte2.getHealth());
    }

    @Test
    public void testAttackHero() {
        assertEquals(Constants.HEROHEALTHMAX, player2.getHero().getHealth());

        player1.attack(carte1.getId(), "hero");

        assertEquals(29, player2.getHero().getHealth());
    }
}