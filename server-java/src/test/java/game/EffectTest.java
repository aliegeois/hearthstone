package test.java.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;

import game.Player;
import game.hero.Hero;
import game.hero.HeroMage;
import game.hero.HeroWarrior;
import game.CardMinion;
import game.CardSpell;
import game.Constants;

import game.effect.*;

public class EffectTest{

    private Effect effet;
    private CardSpell carte;

    private Player player1, player2;
    private Hero hero1, hero2;
    private CardMinion carteMin1, carteMin2, carteMin3, carteMin4;

    private Set<SingleTargetEffect> ste;
    private Set<MultipleTargetEffect> mte;
    private Set<GlobalEffect> gte;

    @BeforeEach

    void Init(){

        this.player1 = new Player("Billy", "warrior");
        this.player2 = new Player("Bob", "mage");
        this.hero1 = new HeroWarrior(player1);
        this.hero2 = new HeroMage(player2);

        this.carteMin1 = new CardMinion(1, player1, "carteMin1", 1, 1, 1, new HashSet<String>(), new HashMap<String, Integer>());
        this.carteMin2 = new CardMinion(2, player1, "carteMin2", 1, 1, 1, new HashSet<String>(), new HashMap<String, Integer>());
        this.carteMin3 = new CardMinion(3, player2, "carteMin3", 1, 1, 1, new HashSet<String>(), new HashMap<String, Integer>());
        this.carteMin4 = new CardMinion(4, player2, "carteMin4", 1, 1, 1, new HashSet<String>(), new HashMap<String, Integer>());

        this.ste = new HashSet<SingleTargetEffect>();
        this.mte = new HashSet<MultipleTargetEffect>();
        this.gte = new HashSet<GlobalEffect>();
    }

    @Test

    void testDrawRandom() {

        carte = new CardSpell(0, player1, "Sprint", 7, ste, mte, gte);
        GlobalEffect effect = new DrawRandom(carte, 4);
        carte.addEffect(effect);

        assertEquals(0,player1.getHand().size());

        player1.getHand().putIfAbsent(0, carte);

        assertEquals(1, player1.getHand().size());

    }
}