package test.java.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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

        this.player1 = new Player("Billy", "Warrior");
        this.player2 = new Player("Bob", "Mage");
        this.hero1 = new HeroWarrior(player1);
        this.hero2 = new HeroMage(player2);

        this.carteMin1 = new CardMinion("1", player1, "carteMin1", 1, 1, 1, new HashSet<String>(), new HashMap<String, Integer>());
        this.carteMin2 = new CardMinion("2", player1, "carteMin2", 1, 1, 1, new HashSet<String>(), new HashMap<String, Integer>());
        this.carteMin3 = new CardMinion("3", player2, "carteMin3", 1, 1, 1, new HashSet<String>(), new HashMap<String, Integer>());
        this.carteMin4 = new CardMinion("4", player2, "carteMin4", 1, 1, 1, new HashSet<String>(), new HashMap<String, Integer>());

        this.player1.getDeck().add(carteMin1);
        this.player1.getDeck().add(carteMin2);
        this.player2.getDeck().add(carteMin3);
        this.player2.getDeck().add(carteMin4);

        this.ste = new HashSet<SingleTargetEffect>();
        this.mte = new HashSet<MultipleTargetEffect>();
        this.gte = new HashSet<GlobalEffect>();

    }

    @Test

    void testDrawRandom() {

        carte = new CardSpell("0", player1, "Sprint", 7, ste, mte, gte);
        GlobalEffect effect = new DrawRandom(carte, 2);
        carte.addEffect(effect);
        
        assertEquals(0, player1.getHand().size());
        assertEquals(0, player1.getBoard().size());
        assertEquals(2, player1.getDeck().size());

        player1.getHand().put("0", carte);

        assertEquals(1, player1.getHand().size());

        effect.play();

        assertEquals(3, player1.getHand().size());

        System.out.println(carte);
    }

    @Test

    void testMultTarBuffHealthOwnBoard(){

        carte = new CardSpell("0", player1, "test", 7, ste, mte, gte);
        MultipleTargetEffect effect = new MultiTargetBuff(carte, true, false, false, false, 2, 0);
        carte.addEffect(effect);

        assertEquals(1, carteMin1.getHealth());
        assertEquals(1, carteMin2.getHealth());
        assertEquals(1, carteMin1.getDamage());

        effect.play();
     
        //assertEquals(3, carteMin1.getHealth());
        //assertEquals(3, carteMin2.getHealth());

        assertEquals(3, carteMin1.getDamage());
        
    }
}