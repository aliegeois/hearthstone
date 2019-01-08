package test.java.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import game.CardMinion;
import game.Player;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CardMinionTest {

    Player player1 = new Player("Billy", "Warrior");
    Set<String> cap = Collections.emptySet();
    Map<String,Integer> boost = Collections.emptyMap();

    CardMinion carte1 ;

    @BeforeEach
    public void Init(){
        carte1 = new CardMinion(1, player1, "mes_couilles", 2, 7, 4, cap, boost);
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
}