package fr.ministone.game.hero;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;

import fr.ministone.game.Player;
import fr.ministone.game.hero.HeroMage;
import fr.ministone.game.hero.HeroPaladin;
import fr.ministone.game.hero.HeroWarrior;
import fr.ministone.game.card.CardMinion;
import fr.ministone.game.Constants;

public class HeroTest {
	
	private Player player1;
	private Player player2;
	private Player player3;
	private HeroWarrior hero1;
	private HeroMage hero2;
	private HeroPaladin hero3;
	private CardMinion minion1;

	@BeforeEach
	public void init() {
        player1 = new Player("Billy", "a", "warrior");
        player2 = new Player("Bob", "b", "mage");
        player3 = new Player("Tiburs", "c", "paladin");
        hero1 = (HeroWarrior)player1.getHero();
        hero2 = (HeroMage)player2.getHero();
        hero3 = (HeroPaladin)player3.getHero();

		minion1 = new CardMinion("1", "shared", player1, "carte1", 1, 0, 3, new HashSet<String>(), new HashMap<String, Integer>());

		//this.hero1 = new HeroWarrior();
		//this.hero2 = new HeroMage();
		//this.hero3 = new HeroPaladin(player3);

	}

	@Test
	void testSpecialHero1() {
		assertEquals(0, hero1.getArmor());
		hero1.special();
		assertEquals(2, hero1.getArmor());
	}

	@Test
	void testSpecialHero2() {
		assertEquals(3, minion1.getHealth());
		hero2.special(minion1);
		assertEquals(1, minion1.getHealth());
	}

	@Test
	void testSpecialHero3() {
		//Test hero3
        assertEquals(0, player3.getBoard().size(), "player3.board n'est pas vide");
        player3.nextTurn();
        assertEquals(1, player3.getBoard().size(), "player3.board ne contient pas 1 carte");
        hero3.special();
		assertEquals(2, player3.getBoard().size(), "player3.board ne contient pas 2 cartes");
	}
		

	@Test
	void testTakeDamage() {
		hero1.special();

		assertEquals(Constants.HEROHEALTHMAX, hero1.getHealth());
		assertEquals(Constants.HEROHEALTHMAX, hero2.getHealth());
		assertEquals(2, hero1.getArmor());
		assertEquals(0, hero2.getArmor());

		hero1.takeDamage(1);
		hero2.takeDamage(1);

		assertEquals(Constants.HEROHEALTHMAX, hero1.getHealth());
		assertEquals(29, hero2.getHealth());
		assertEquals(1, hero1.getArmor());

		hero1.takeDamage(5);

		assertEquals(26, hero1.getHealth());
		assertEquals(0, hero1.getArmor());
	}

	@Test
	void testHeal() {
		hero1.takeDamage(4);
		assertEquals(Constants.HEROHEALTHMAX-4, hero1.getHealth());
		hero1.heal(1);
		assertEquals(Constants.HEROHEALTHMAX - 3, hero1.getHealth());
		hero1.heal(9);
		assertEquals(Constants.HEROHEALTHMAX, hero1.getHealth());
		
	}
	
	@Test
	void testBuffHealth() {
		assertEquals(Constants.HEROHEALTHMAX, hero1.getHealth());
		hero1.buffHealth(10);
		assertEquals(Constants.HEROHEALTHMAX + 10, hero1.getHealth());
	}

	@Test
	void testBoostArmor() {
		assertEquals(0, hero1.getArmor());
		hero1.buffArmor(1);
		assertEquals(1, hero1.getArmor());
	}

	@Test
	void testIsDead() {
		assertFalse(hero1.isDead());
		hero1.takeDamage(hero1.getHealth());
		assertTrue(hero1.isDead());
	}
}
