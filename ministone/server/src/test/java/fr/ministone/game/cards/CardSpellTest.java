package fr.ministone.game.cards;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ministone.game.effect.*;
import fr.ministone.game.card.*;
import fr.ministone.User;
import fr.ministone.game.*;

public class CardSpellTest{

    private IGame game;
    private CardSpell carte;

    private IPlayer player1;
    private IPlayer player2;
    private User user1, user2;
    private CardMinion carteMin1, carteMin2, carteMin3, carteMin4;

    private Set<SingleTargetEffect> ste;
    private Set<MultipleTargetEffect> mte;
    private Set<GlobalEffect> gte;

    @BeforeEach
    private void Init(){

        this.user1 = new User("Billy", "E");
        this.user2 = new User("Bob", "F");

        game = new GameMock(user1,user2); 

        this.player1 = this.game.getPlayer("E");
        this.player2 = this.game.getPlayer("F");    

        this.carteMin1 = new CardMinion("1", "shared", this.player1, "carteMin1", 1, 1, 1, new HashSet<String>(), new HashMap<String, Integer>());
        this.carteMin2 = new CardMinion("2", "shared", this.player1, "carteMin2", 1, 1, 3, new HashSet<String>(), new HashMap<String, Integer>());
        this.carteMin3 = new CardMinion("3", "shared", this.player2, "carteMin3", 1, 1, 1, new HashSet<String>(), new HashMap<String, Integer>());
        this.carteMin4 = new CardMinion("4", "shared", this.player2, "carteMin4", 1, 1, 3, new HashSet<String>(), new HashMap<String, Integer>());

        this.player1.getDeck().add(carteMin1);
        this.player1.getDeck().add(carteMin2);
        this.player2.getDeck().add(carteMin3);
        this.player2.getDeck().add(carteMin4);

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

        SingleTargetEffect effet1 = new SingleTargetDamage(3);
        MultipleTargetEffect effet2 = new MultipleTargetDamage(true, true, false, false,1);

        this.ste.add(effet1);
        this.mte.add(effet2);

        carte = new CardSpell("0", "shared", this.player1, "test", 1, ste, mte, gte);

        assertEquals(2, this.player1.getBoard().size());
        assertEquals(2, this.player2.getBoard().size());

        carte.play(player2.getHero());
        game.checkBoard();

        assertEquals(27, this.player2.getHero().getHealth());
        assertEquals(1, this.player1.getBoard().size());
        assertEquals(1, this.player2.getBoard().size());

    }

    @Test
    public void testCardSpellHeal(){
        
        SingleTargetEffect effet1 = new SingleTargetHeal(3);
        MultipleTargetEffect effet2 = new MultipleTargetHeal(true, true, false, false, 1);

        this.ste.add(effet1);
        this.mte.add(effet2);

        carte = new CardSpell("0", "shared", this.player1, "test", 1, ste, mte, gte);

        player1.getHero().takeDamage(4);
        carteMin2.takeDamage(2);
        assertEquals(26, this.player1.getHero().getHealth());
        assertEquals(1, carteMin2.getHealth());

        carte.play(this.player1.getHero());

        assertEquals(29, this.player1.getHero().getHealth());
        assertEquals(1, carteMin1.getHealth());
        assertEquals(2, carteMin2.getHealth());

    }

    @Test 
    public void testCardSpellTransform() {
        IEntity mouton = new CardMinion(carteMin3.getId(), "shared", carteMin1.getOwner(), "mouton", 0, 0, 1, new HashSet<String>(), new HashMap<String, Integer>());
        SingleTargetEffect effet1 = new Transform(mouton);

        this.ste.add(effet1);

        carte = new CardSpell("0", "shared", this.player1, "test", 1, ste, mte, gte);

        assertEquals("carteMin1", carteMin1.getName());
        carte.play(carteMin1);
        assertEquals("mouton", carteMin1.getName());
    }

    @Test
    public void testDraw(){
        GlobalEffect effet = new DrawRandom(2);

        this.gte.add(effet);

        carte = new CardSpell("0", "shared", this.player1, "test", 1, ste, mte, gte);
        
        assertEquals(0, this.player1.getHand().size());

        carte.play();

        assertEquals(2, this.player1.getHand().size());
    }
}