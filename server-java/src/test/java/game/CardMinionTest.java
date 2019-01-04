package test.java.game;

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

    CardMinion carte1 = new CardMinion(1, player1, "mes_couilles", 2, 7, 4, cap, boost);
}