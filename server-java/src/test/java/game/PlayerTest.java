package test.java.game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import game.CardMinion;
import game.Player;
import game.hero.HeroMage;
import game.hero.HeroWarrior;

public class PlayerTest{

    Player player1 = new Player("Billy", "Warrior");
    Player player2 = new Player("Bob", "Mage");
    Set<String> cap1 = new HashSet<String>();
    Set<String> cap2 = new HashSet<String>();

    HeroWarrior hero1;
    HeroMage hero2;
    
    CardMinion carte1, carte2;


    @BeforeEach
    public void Init(){

        this.hero1 = new HeroWarrior(player1);
        this.hero2 = new HeroMage(player2);
        this.cap1.add("ready");
        this.cap2.add("provocation");
        this.carte1 = new CardMinion(1, player1, "carte1", 1, 1, 1, cap1, new HashMap<String,Integer>());
        this.carte2 = new CardMinion(2, player2, "carte2", 1, 1, 1, cap2, new HashMap<String,Integer>());

        this.player1.getDeck().add(carte1);
        this.player2.getDeck().add(carte2);

    }

    @Test

    void testDrawCard(){
        assertEquals(0, player1.getHand().size());
        player1.drawCard();
        assertEquals(1, player1.getHand().size());
    }

    @Test

    void testPlayMinion(){
        player1.drawCard();
        assertTrue(player1.getHand().size() == 1);
        assertEquals(0, player1.getBoard().size());

        player1.playMinion(1);
        assertEquals(0, player1.getHand().size());
        assertEquals(1, player1.getBoard().size());
    }

    @Test

    void testSetOpponent(){
        player1.setOpponent(player2);

        assertEquals(player2, player1.getOpponent());
        assertEquals(player1, player2.getOpponent());
    }
}