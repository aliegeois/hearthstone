package test.java.game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import game.CardMinion;
import game.Constants;
import game.Player;
import game.hero.HeroMage;
import game.hero.HeroWarrior;

public class PlayerTest{

    private Player player1 = new Player("Billy", "Warrior");
    private Player player2 = new Player("Bob", "Mage");
    private Set<String> cap1 = new HashSet<String>();
    private Set<String> cap2 = new HashSet<String>();

    private HeroWarrior hero1;
    private HeroMage hero2;
    
    private CardMinion carte1, carte2;


    @BeforeEach
    public void Init(){

        this.hero1 = new HeroWarrior(player1);
        this.hero2 = new HeroMage(player2);
        this.cap1.add("ready");
        this.cap2.add("provocation");
        this.carte1 = new CardMinion("1", player1, "carte1", 1, 1, 10, cap1, new HashMap<String,Integer>());
        this.carte2 = new CardMinion("2", player2, "carte2", 1, 1, 10, cap2, new HashMap<String,Integer>());

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
        String idCard = player1.drawCard();
        assertTrue(player1.getHand().size() == 1);
        assertEquals(0, player1.getBoard().size());

        player1.playMinion(idCard);
        assertEquals(0, player1.getHand().size());
        assertEquals(1, player1.getBoard().size());
    }

    @Test

    void testSetOpponent(){
        player1.setOpponent(player2);

        assertEquals(player2, player1.getOpponent());
        assertEquals(player1, player2.getOpponent());
    }

    @Test 

    void testAttackMinion(){
        assertEquals(10, carte1.getHealth());
        assertEquals(10, carte2.getHealth());
        player1.attack(carte1, carte2);

        assertEquals(9, carte1.getHealth());
        assertEquals(9, carte2.getHealth());
    }

    @Test
    void testAttackHero(){
        assertEquals(Constants.HEROMAXHEALTH, hero2.getHealth());

        player1.attack(carte1, hero2);

        assertEquals(29, hero2.getHealth());
    }
}