package test.java.game.hero;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import game.Player;
import game.hero.Hero;
import game.hero.HeroMage;
import game.hero.HeroPaladin;
import game.hero.HeroWarrior;

public class HeroTest {
	
	Player player1 = new Player("Billy", "Warrior");
	Player player2 = new Player("Bob", "Mage");
	Player player3 = new Player("Tiburs", "Paladin");
	
	HeroWarrior hero1 = new HeroWarrior(player1);
	HeroMage hero2 = new HeroMage(player2);
	HeroPaladin hero3 = new HeroPaladin(player3);

	@Test
	void testSpecial() {
		assertEquals(0, hero1.getArmor());
		hero1.special();
		assertEquals(2, hero1.getArmor());
	}
	
	@Test
	void testTakeDamage() {
		
	}
	
	@Test
	void testHeal() {
		
	}
	
	@Test
	void testBoostHealth() {
		
	}
}
