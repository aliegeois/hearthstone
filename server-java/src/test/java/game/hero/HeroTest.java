package test.java.game.hero;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

import game.Player;
import game.hero.HeroMage;
import game.hero.HeroPaladin;
import game.hero.HeroWarrior;
import game.Constants;

public class HeroTest {
	
	private Player player1 = new Player("Billy", "Warrior");
	private Player player2 = new Player("Bob", "Mage");
	private Player player3 = new Player("Tiburs", "Paladin");
	private HeroWarrior hero1;
	private HeroMage hero2;
	private HeroPaladin hero3;

	@BeforeEach
	public void Init(){
		
		this.hero1 = new HeroWarrior(player1);
		this.hero2 = new HeroMage(player2);
		this.hero3 = new HeroPaladin(player3);

	}

	@Test
	void testSpecial() {
		assertEquals(0, hero1.getArmor());
		hero1.special();
		assertEquals(2, hero1.getArmor());

		//TODO: faire le test pour les autres heros
	}
		

	@Test
	void testTakeDamage(){

		hero1.special();

		assertEquals(Constants.HEROMAXHEALTH, hero1.getHealth());
		assertEquals(Constants.HEROMAXHEALTH, hero2.getHealth());
		assertEquals(2, hero1.getArmor());
		assertEquals(0, hero2.getArmor());

		hero1.takeDamage(1);
		hero2.takeDamage(1);

		assertEquals(Constants.HEROMAXHEALTH, hero1.getHealth());
		assertEquals(29, hero2.getHealth());
		assertEquals(1, hero1.getArmor());

		hero1.takeDamage(5);

		assertEquals(26, hero1.getHealth());
		assertEquals(0, hero1.getArmor());

	}

	@Test
	void testHeal() {
		hero1.takeDamage(4);
		assertEquals(Constants.HEROMAXHEALTH-4, hero1.getHealth());
		hero1.heal(1);
		assertEquals(Constants.HEROMAXHEALTH - 3, hero1.getHealth());
		hero1.heal(9);
		assertEquals(Constants.HEROMAXHEALTH, hero1.getHealth());
		
	}
	
	@Test
	void testBoostHealth() {

		assertEquals(Constants.HEROMAXHEALTH, hero1.getHealth());
		hero1.boostHealth(10);
		assertEquals(Constants.HEROMAXHEALTH + 10, hero1.getHealth());
		
	}

	@Test
	void testBoostArmor() {

		assertEquals(0, hero1.getArmor());
		hero1.boostArmor(1);
		assertEquals(1, hero1.getArmor());

	}

	@Test
	void testIsDead(){

		assertFalse(hero1.isDead());
		hero1.takeDamage(hero1.getHealth());
		assertTrue(hero1.isDead());

	}
}
