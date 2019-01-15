package fr.ministone.game.hero;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;

import fr.ministone.game.card.CardMinion;
import fr.ministone.repository.CardMinionRepository;
import fr.ministone.repository.MCardMinionRepository;
import fr.ministone.game.Constants;
import fr.ministone.game.IEntity;
import fr.ministone.game.IPlayer;
import fr.ministone.game.PlayerMock;

public class HeroTest {
	private IPlayer player1, player2;
	private PlayerMock player3;

	private HeroWarrior hero1;
	private HeroMage hero2;
	private HeroPaladin hero3;

	private CardMinion card;
	private CardMinionRepository minionRepository = new MCardMinionRepository();
	

	@BeforeEach
	public void init() {
		player1 = new PlayerMock("warrior");
		player2 = new PlayerMock("mage");
		player3 = new PlayerMock("paladin");

		CardMinion recrue = new CardMinion(8l, "paladin", player3, "Recrue de la Main d'argent", 1, 1, 1, new HashSet<String>(), new HashMap<String, Integer>());

		minionRepository.save(recrue);

		player3.setCardMinionRepository(minionRepository);
		
		card = new CardMinion(1l, "shared", null, "Card Minion 1", 1, 0, 3, new HashSet<String>(), new HashMap<String, Integer>());

		hero1 = (HeroWarrior)player1.getHero();
		hero2 = (HeroMage)player2.getHero();
		hero3 = (HeroPaladin)player3.getHero();

	}

	@Test
	void testSpecialHero1() {
		assertEquals(0, hero1.getArmor());

		hero1.special();

		assertEquals(2, hero1.getArmor());
	}

	@Test
	void testSpecialHero2() {
		assertEquals(3, card.getHealth());

		hero2.special(card);

		assertEquals(1, card.getHealth());
	}

	@Test
	void testSpecialHero3() {
		assertTrue(player3.getBoard().isEmpty());
		assertTrue(player3.getHand().isEmpty());

		hero3.special();

		assertEquals(1, player3.getBoard().size());
	}
		

	@Test
	void testTakeDamage() {
		player1.heroSpecial();

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

		assertEquals(Constants.HEROHEALTHMAX - 4, hero1.getHealth());

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
	void testIsDead() {
		assertFalse(hero1.isDead());

		hero1.takeDamage(hero1.getHealth());

		assertTrue(hero1.isDead());
	}
}
