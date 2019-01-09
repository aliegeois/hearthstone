package test.java.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import game.CardMinion;
import game.Player;

public class PlayerTest{

    Player player1 = new Player("Billy", "Warrior");
    Player player2 = new Player("Bob", "Mage");
    Set<String> cap1 = new HashSet<String>();
    Set<String> cap2 = new HashSet<String>();
    
    CardMinion carte1, carte2;


    @BeforeEach
    public void Init(){

        cap1.add("ready");
        cap2.add("provocation");
        this.carte1 = new CardMinion(1, player1, "carte1", 1, 1, 1, cap1, new HashMap<String,Integer>());
        this.carte2 = new CardMinion(2, player2, "carte2", 1, 1, 1, cap2, new HashMap<String,Integer>());
    }

    @Test

    void testPlayMinion(){

    }
}