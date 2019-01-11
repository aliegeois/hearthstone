package game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import game.hero.Hero;
import game.hero.HeroMage;
import game.hero.HeroWarrior;

public class GameTest{

    private Game game1, game2;
    private Player player1, player2;
    private Hero hero1, hero2;
    private CardMinion carte1, carte2;
    private CardSpell carte3;


    @BeforeEach
    public void Init(){

        player1 = new Player("Billy");
        player2 = new Player("Bob");

        hero1 = new HeroWarrior(player1);
        hero2 = new HeroMage(player2);

    }

    @Test
    public void testInit(){
        game1 = new Game();
        assertFalse(game1.isReady());

        game1.addPlayer(player1);
        game1.addPlayer(player2);
        assertTrue(game1.isReady());

        game2 = new Game(player1, player2);
        assertTrue(game2.isReady());
    }
}