package test.java.game;

import java.util.HashMap;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import game.CardMinion;
import game.Player;

public class PlayerTest{

    Player player1 = new Player("Billy", "Warrior");
    Player player2 = new Player("Bob", "Mage");
    CardMinion carte1 = new CardMinion(1, player1, "carte1", 1, 1, 1, new HashSet<String>(), new HashMap<String,Integer>());

    @BeforeEach
    public void Init(){

    }

    @Test

    void testPlayMinion(){

    }
}