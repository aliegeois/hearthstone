package test.java.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;

import main.java.game.Player;
import main.java.game.hero.Hero;
import main.java.game.hero.HeroMage;
import main.java.game.hero.HeroWarrior;
import main.java.game.CardMinion;
import main.java.game.CardSpell;
import main.java.game.Constants;
import main.java.game.Game;

import main.java.game.effect.*;

public class EffectTest{

    private Game game;
    private CardSpell carte;

    private Player player1, player2;
    private Hero hero1, hero2;
    private CardMinion carteMin1, carteMin2, carteMin3, carteMin4;

    private Set<SingleTargetEffect> ste;
    private Set<MultipleTargetEffect> mte;
    private Set<GlobalEffect> gte;

    @BeforeEach

    void Init(){

        this.player1 = new Player("Billy");
        this.player2 = new Player("Bob");
        
        game = new Game(player1,player2);        

        this.hero1 = new HeroWarrior(player1);
        this.hero2 = new HeroMage(player2);

        this.carteMin1 = new CardMinion("1", player1, "carteMin1", 1, 1, 1, new HashSet<String>(), new HashMap<String, Integer>());
        this.carteMin2 = new CardMinion("2", player1, "carteMin2", 1, 1, 3, new HashSet<String>(), new HashMap<String, Integer>());
        this.carteMin3 = new CardMinion("3", player2, "carteMin3", 1, 1, 1, new HashSet<String>(), new HashMap<String, Integer>());
        this.carteMin4 = new CardMinion("4", player2, "carteMin4", 1, 1, 3, new HashSet<String>(), new HashMap<String, Integer>());

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
    }

    @Test

    void testMultTarBuffOwnBoard(){

        carte = new CardSpell("0", player1, "test", 7, ste, mte, gte);
        MultipleTargetEffect effect = new MultiTargetBuff(carte, true, false, false, false, 2, 0);
        carte.addEffect(effect);

        
        player1.getBoard().put(carteMin1.getId(), carteMin1);
        player1.getBoard().put(carteMin2.getId(), carteMin2);

        effect.play();
     
        assertEquals(3, carteMin1.getHealth());
        assertEquals(5, carteMin2.getHealth());
    }

    @Test

    void testMultTarBuffOpponentBoard(){
        carte = new CardSpell("0", player1, "test", 7, ste, mte, gte);
        MultipleTargetEffect effect = new MultiTargetBuff(carte, false, true, false, false, 0, 2);
        carte.addEffect(effect);

        player2.getBoard().put(carteMin3.getId(), carteMin3);
        player2.getBoard().put(carteMin4.getId(), carteMin4);

        effect.play();
     
        assertEquals(3, carteMin3.getDamage());
        assertEquals(3, carteMin4.getDamage());
    }

    @Test

    void testMultTarDamageAll(){
        carte = new CardSpell("0", player1, "test", 7, ste, mte, gte);
        MultipleTargetEffect effect = new MultiTargetDamage(carte, true, true, true, true, 2);
        carte.addEffect(effect);

        player1.getBoard().put(carteMin1.getId(), carteMin1);
        player1.getBoard().put(carteMin2.getId(), carteMin2);
        player2.getBoard().put(carteMin3.getId(), carteMin3);
        player2.getBoard().put(carteMin4.getId(), carteMin4);

        effect.play();
        game.checkBoard();
        
        assertEquals(Constants.HEROMAXHEALTH -2, hero1.getHealth());
        assertEquals(Constants.HEROMAXHEALTH -2, hero2.getHealth());

        assertEquals(1, player1.getBoard().size());
        assertEquals(1, player2.getBoard().size()); 
    }
}