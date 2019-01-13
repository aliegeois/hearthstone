package fr.ministone.game.effect;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;

import fr.ministone.User;
import fr.ministone.game.*;
import fr.ministone.game.hero.*;
import fr.ministone.game.card.CardMinion;
import fr.ministone.game.card.CardSpell;
import fr.ministone.game.Constants;

public class EffectTest{

    private IGame game;
    private CardSpell carte;

    private IPlayer player1, player2;
    private Hero hero1, hero2;
    private User user1, user2;
    private CardMinion carteMin1, carteMin2, carteMin3, carteMin4;

    private Set<SingleTargetEffect> ste;
    private Set<MultipleTargetEffect> mte;
    private Set<GlobalEffect> gte;

    @BeforeEach
    public void Init(){

        this.user1 = new User("Billy", "E");
        this.user2 = new User("Bob", "E");
        
        game = new GameMock(user1, user2);
        
        this.player1 = this.game.getPlayer("E");
        this.player2 = this.game.getPlayer("F");

        this.carteMin1 = new CardMinion("1", "shared", player1, "carteMin1", 1, 1, 1, new HashSet<String>(), new HashMap<String, Integer>());
        this.carteMin2 = new CardMinion("2", "shared", player1, "carteMin2", 1, 1, 3, new HashSet<String>(), new HashMap<String, Integer>());
        this.carteMin3 = new CardMinion("3", "shared", player2, "carteMin3", 1, 1, 1, new HashSet<String>(), new HashMap<String, Integer>());
        this.carteMin4 = new CardMinion("4", "shared", player2, "carteMin4", 1, 1, 3, new HashSet<String>(), new HashMap<String, Integer>());

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

        GlobalEffect effect = new DrawRandom(2);
        this.gte.add(effect);
        carte = new CardSpell("0", "shared", player1, "Sprint", 7, ste, mte, gte);
        
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

        MultipleTargetEffect effect = new MultipleTargetBuff(true, false, false, false, 2, 0);
        this.mte.add(effect);
        carte = new CardSpell("0", "shared", player1, "Sprint", 7, ste, mte, gte);

        
        player1.getBoard().put(carteMin1.getId(), carteMin1);
        player1.getBoard().put(carteMin2.getId(), carteMin2);

        effect.play();
     
        assertEquals(3, carteMin1.getHealth());
        assertEquals(5, carteMin2.getHealth());
    }

    @Test
    void testMultTarBuffOpponentBoard(){
        MultipleTargetEffect effect = new MultipleTargetBuff(false, true, false, false, 0, 2);
        this.mte.add(effect);
        carte = new CardSpell("0", "shared", player1, "Sprint", 7, ste, mte, gte);

        player2.getBoard().put(carteMin3.getId(), carteMin3);
        player2.getBoard().put(carteMin4.getId(), carteMin4);

        effect.play();
     
        assertEquals(3, carteMin3.getDamage());
        assertEquals(3, carteMin4.getDamage());
    }

    @Test
    void testMultTarDamageAll(){
        MultipleTargetEffect effect = new MultipleTargetDamage(true, true, true, true, 2);
        this.mte.add(effect);
        carte = new CardSpell("0", "shared", player1, "Sprint", 7, ste, mte, gte);

        player1.getBoard().put(carteMin1.getId(), carteMin1);
        player1.getBoard().put(carteMin2.getId(), carteMin2);
        player2.getBoard().put(carteMin3.getId(), carteMin3);
        player2.getBoard().put(carteMin4.getId(), carteMin4);

        effect.play();
        game.checkBoard();
        
        assertEquals(Constants.HEROHEALTHMAX -2, this.player1.getHero().getHealth());
        assertEquals(Constants.HEROHEALTHMAX -2, this.player1.getHero().getHealth());

        assertEquals(1, player1.getBoard().size());
        assertEquals(1, player2.getBoard().size()); 
    }

    @Test 
    void testMultTarHealBothHeroes(){

        MultipleTargetEffect effect = new MultipleTargetHeal(false, false, true, true, 5);
        this.mte.add(effect);
        carte = new CardSpell("0", "shared", player1, "Sprint", 7, ste, mte, gte);

        this.player1.getHero().takeDamage(2);
        this.player2.getHero().takeDamage(8);

        assertEquals(28, this.player1.getHero().getHealth());
        assertEquals(22, this.player2.getHero().getHealth());

        effect.play();

        assertEquals(Constants.HEROHEALTHMAX, this.player1.getHero().getHealth());
        assertEquals(27, this.player2.getHero().getHealth());
        
    }

    @Test
    void testSinglTarDamBuff(){

        SingleTargetEffect effect = new SingleTargetDamageBuff(1);
        this.ste.add(effect);
        carte = new CardSpell("0", "shared", player1, "Sprint", 7, ste, mte, gte);

        assertEquals(1, carteMin1.getDamage());

        effect.play(carteMin1);

        assertEquals(2, carteMin1.getDamage());
    }

    @Test
    void testSinglTarHealthBuff(){

        SingleTargetEffect effect = new SingleTargetLifeBuff(1);
        this.ste.add(effect);
        carte = new CardSpell("0", "shared", player1, "Sprint", 7, ste, mte, gte);

        assertEquals(1, carteMin3.getHealth());

        effect.play(carteMin3);

        assertEquals(2, carteMin3.getHealth());
    }

    @Test
    void testSinglTargDamage(){

        SingleTargetEffect effect = new SingleTargetDamage(10);
        this.ste.add(effect);
        carte = new CardSpell("0", "shared", player1, "Sprint", 7, ste, mte, gte);

        assertEquals(Constants.HEROHEALTHMAX, this.player1.getHero().getHealth());

        effect.play(this.player1.getHero());

        assertEquals(20, this.player1.getHero().getHealth());
    }

    @Test
    void testTransform(){
        carte = new CardSpell("0", "shared", player1, "test", 7, ste, mte, gte);
        IEntity mouton = new CardMinion(carteMin3.getId(), "shared", player2, "mouton", 0, 0, 1, new HashSet<String>(), new HashMap<String, Integer>());
        SingleTargetEffect effect = new Transform(mouton);


        player2.getBoard().put(carteMin3.getId(), carteMin3);

        effect.play(carteMin3);

        assertEquals("mouton", player2.getBoard().get("3").getName());
    }
}