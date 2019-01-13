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
	
	private Player player1 = new Player("Billy", "E", "warrior");
	private Player player2 = new Player("Bob", "F", "mage");
	private Player player3 = new Player("Tiburs", "G", "paladin");
	private HeroWarrior hero1;
	private HeroMage hero2;
	private HeroPaladin hero3;
	private CardMinion carte1;

	@BeforeEach
	public void Init(){
		
		this.carte1 = new CardMinion("1", "shared", player1, "carte1", 1, 0, 3, new HashSet<String>(), new HashMap<String, Integer>());

		this.hero1 = new HeroWarrior();
		this.hero2 = new HeroMage();
		this.hero3 = new HeroPaladin();

		this.hero3.setPlayer(player3);

	}

	@Test
	void testSpecialHero1() {
		assertEquals(0, hero1.getArmor());
		hero1.special();
		assertEquals(2, hero1.getArmor());
	}

	@Test

	void testSpecialHero2(){
		assertEquals(3, carte1.getHealth());
		hero2.special(carte1);
		assertEquals(1, carte1.getHealth());
		
	}

	@Test
	void testSpecialHero3(){
		//Test hero3
		assertTrue(player3.getBoard().isEmpty());
		assertTrue(player3.getHand().isEmpty());
		hero3.special();
		assertEquals(1, player3.getBoard().size());
		
	}
		

	@Test
	void testTakeDamage(){

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
	void testBoostHealth() {

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
	void testIsDead(){

		assertFalse(hero1.isDead());
		hero1.takeDamage(hero1.getHealth());
		assertTrue(hero1.isDead());

	}
}
