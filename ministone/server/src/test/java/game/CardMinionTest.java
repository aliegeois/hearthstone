package game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ministone.game.card.CardMinion;
import fr.ministone.game.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CardMinionTest {

    private Player player1 = new Player("Billy","E");
    private Player player2 = new Player("Bob","E");
    private Set<String> cap1 = new HashSet<String>();
    private Set<String> cap2 = new HashSet<String>();
    private String prov = "provocation";
    private String ready = "charge";
    private Map<String,Integer> boost = new HashMap<String, Integer>();

    private CardMinion carte1, carte2;

    @BeforeEach
    public void Init(){
        this.cap1.add(ready);
        this.cap2.add(prov);
        this.carte1 = new CardMinion("1", player1, "Carte1", 2, 7, 4, cap1, boost);
        this.carte2 = new CardMinion("2", player2, "Carte2", 1, 2, 10, cap2, boost);
    }


    @Test

    void testTakeDamage(){
        assertEquals(carte1.getHealth(),4);
        carte1.takeDamage(2);
        assertEquals(carte1.getHealth(), 2);
    }

    @Test

    void testIsDead(){
        assertFalse(carte1.isDead());
        carte1.takeDamage(4);
        assertTrue(carte1.isDead());
    }

    @Test

    void testAttack(){
        assertEquals(carte2.getHealth(), 10);
        carte1.attack(carte2);
        assertEquals(carte2.getHealth(), 3);
    }

    @Test 

    void testHeal(){
        assertEquals(carte1.getHealth(), 4);
        carte1.takeDamage(3);
        assertEquals(carte1.getHealth(), 1);
        carte1.heal(2);
        assertEquals(carte1.getHealth(), 3);

        carte1.heal(2);
        assertEquals(carte1.getHealth(), 4);
    }

    @Test

    void testBoostDamage(){
        assertEquals(carte1.getDamage(), 7);
        carte1.buffDamage(10);
        assertEquals(carte1.getDamage(), 17);

        carte1.buffDamage(-10);
        assertEquals(carte1.getDamage(), 7);
    }

    @Test

    void testBoostHealth(){
        assertEquals(carte1.getHealth(), 4);
        assertEquals(carte1.getHealthMax(), 4);
        assertEquals(carte1.getHealthBoosted(), 0);

        carte1.buffHealth(2);
        
        assertEquals(carte1.getHealth(), 6);
        assertEquals(carte1.getHealthMax(), 4);
        assertEquals(carte1.getHealthBoosted(), 2);

        carte1.takeDamage(4);

        assertFalse(carte1.isDead());

        carte1.heal(4);

        assertEquals(carte1.getHealth(), carte1.getHealthMax());
    }

    @Test

    void testDie(){
        assertTrue(player1.getBoard().isEmpty());

        player1.getBoard().put("1", carte1);
        carte1.takeDamage(3);

        assertEquals(carte1.getHealth(), 1);

        carte2.attack(carte1);
        carte1.getOwner().checkDead();
        
        assertTrue(carte1.isDead());
        assertTrue(player1.getBoard().isEmpty());
    }

    @Test

    void testIsReady(){
        assertTrue(carte1.isReady());
        assertFalse(carte2.isReady());
    }

    @Test

    void testIsProvoking(){
        assertFalse(carte1.isProvoking());
        assertTrue(carte2.isProvoking());
    }
}