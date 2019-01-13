package game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ministone.User;
import fr.ministone.game.*;
import fr.ministone.game.card.*;
import fr.ministone.game.hero.*;

public class GameTest{

    private IGame game1, game2;
    private PlayerMock player1, player2;
    private User user1, user2;
    private Hero hero1, hero2;
    private CardMinion carte1, carte2;
    private CardSpell carte3;


    @BeforeEach
    public void Init(){

        player1 = new PlayerMock("Billy","E");
        player2 = new PlayerMock("Bob", "F");

        hero1 = new HeroWarrior(player1);
        hero2 = new HeroMage(player2);

        user1 = new User("Pat", "E");
        user2 = new User("Bob", "F");

    }

}