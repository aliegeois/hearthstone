package game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import game.effect.Effect;
import game.effect.GlobalEffect;
import game.effect.MultiTargetDamage;
import game.effect.MultipleTargetEffect;
import game.effect.SingleTargetDamage;
import game.effect.SingleTargetEffect;
import game.hero.Hero;
import game.hero.HeroMage;
import game.hero.HeroWarrior;

public class CardSpellTest{

    private Game game;
    private CardSpell carte;

    private Player player1, player2;
    private Hero hero1, hero2;
    private CardMinion carteMin1, carteMin2, carteMin3, carteMin4;

    private Set<SingleTargetEffect> ste;
    private Set<MultipleTargetEffect> mte;
    private Set<GlobalEffect> gte;

    @BeforeEach
    private void Init(){

        this.player1 = new Player("Billy");
        this.player2 = new Player("Bob");
        
        game = new Game(player1,player2);        

        this.hero1 = new HeroWarrior(player1);
        this.hero2 = new HeroMage(player2);

        this.carteMin1 = new CardMinion("1", player1, "carteMin1", 1, 1, 1, new HashSet<String>(), new HashMap<String, Integer>());
        this.carteMin2 = new CardMinion("2", player1, "carteMin2", 1, 1, 3, new HashSet<String>(), new HashMap<String, Integer>());
        this.carteMin3 = new CardMinion("3", player2, "carteMin3", 1, 1, 1, new HashSet<String>(), new HashMap<String, Integer>());
        this.carteMin4 = new CardMinion("4", player2, "carteMin4", 1, 1, 3, new HashSet<String>(), new HashMap<String, Integer>());

        this.player1.getBoard().put(carteMin1.getId(), carteMin1);
        this.player1.getBoard().put(carteMin2.getId(), carteMin2);
        this.player2.getBoard().put(carteMin3.getId(), carteMin3);
        this.player2.getBoard().put(carteMin4.getId(), carteMin4);

        this.ste = new HashSet<SingleTargetEffect>();
        this.mte = new HashSet<MultipleTargetEffect>();
        this.gte = new HashSet<GlobalEffect>();
    }

    @Test
    public void testCardSpellDamage(){
        carte = new CardSpell("0", player1, "test", 1, ste, mte, gte);
        SingleTargetEffect effet1 = new SingleTargetDamage(carte, 3);
        MultipleTargetEffect effet2 = new MultiTargetDamage(carte, true, true, false, false, 1);

        carte.addEffect(effet1);
        carte.addEffect(effet2);

        assertEquals(2, player1.getBoard().size());
        assertEquals(2, player2.getBoard().size());

        carte.play(hero2);
        game.checkBoard();

        assertEquals(27, hero2.getHealth());
        assertEquals(1, player1.getBoard().size());
        assertEquals(1, player2.getBoard().size());

    }

}