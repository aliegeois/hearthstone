package fr.ministone.game.cards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ministone.game.card.CardMinion;
import fr.ministone.game.IPlayer;
import fr.ministone.game.PlayerMock;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CardMinionTest {
	private IPlayer player1, player2;
	private CardMinion card1, card2;

	@BeforeEach
	public void init() {
		player1 = new PlayerMock("warrior");
		player2 = new PlayerMock("paladin");
		player1.setOpponent(player2);

		Set<String> capacities1 = new HashSet<String>();
		Set<String> capacities2 = new HashSet<String>();
		capacities1.add("charge");
		capacities2.add("provocation");
		card1 = new CardMinion(1l, "shared", player1, "Card Minion 1", 2, 7, 4, capacities1, new HashMap<String, Integer>());
		card2 = new CardMinion(2l, "shared", player2, "Card Minion 2", 1, 2, 10, capacities2, new HashMap<String, Integer>());
	}

	@Test
	void testTakeDamage() {
		assertEquals(card1.getHealth(), 4);

		card1.takeDamage(2);

		assertEquals(card1.getHealth(), 2);
	}

	@Test
	void testIsDead() {
		assertFalse(card1.isDead());

		card1.takeDamage(4);

		assertTrue(card1.isDead());
	}

	@Test
	void testAttack() {
		assertEquals(card2.getHealth(), 10);

		card1.attack(card2);

		assertEquals(card2.getHealth(), 3);
	}

	@Test
	void testHeal() {
		assertEquals(card1.getHealth(), 4);

		card1.takeDamage(3);

		assertEquals(card1.getHealth(), 1);

		card1.heal(2);

		assertEquals(card1.getHealth(), 3);

		card1.heal(2);

		assertEquals(card1.getHealth(), 4);
	}

	@Test
	void testBoostDamage() {
		assertEquals(card1.getDamage(), 7);

		card1.buffDamage(10);

		assertEquals(card1.getDamage(), 17);

		card1.buffDamage(-10);

		assertEquals(card1.getDamage(), 7);
	}

	@Test
	void testBoostHealth() {
		assertEquals(card1.getHealth(), 4);
		assertEquals(card1.getHealthMax(), 4);
		assertEquals(card1.getHealthBoosted(), 0);

		card1.buffHealth(2);
		
		assertEquals(card1.getHealth(), 6);
		assertEquals(card1.getHealthMax(), 4);
		assertEquals(card1.getHealthBoosted(), 2);

		card1.takeDamage(4);

		assertFalse(card1.isDead());

		card1.heal(4);

		assertEquals(card1.getHealth(), card1.getHealthMax());
	}

	@Test
	void testDie() {
		assertTrue(player1.getBoard().isEmpty());

		player1.getBoard().put(card1.getId(), card1);
		card1.takeDamage(3);

		assertEquals(card1.getHealth(), 1);

		card2.attack(card1);
		card1.getOwner().checkDead();
		
		assertTrue(card1.isDead());
		assertTrue(player1.getBoard().isEmpty());
	}

	@Test
	void testIsReady() {
		assertTrue(card1.isReady());
		assertFalse(card2.isReady());
	}

	@Test
	void testIsProvoking() {
		assertFalse(card1.isProvoking());
		assertTrue(card2.isProvoking());
	}
}