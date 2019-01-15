package fr.ministone.game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ministone.game.card.Card;
import fr.ministone.game.card.CardMinion;
import fr.ministone.game.card.CardSpell;
import fr.ministone.game.Constants;
import fr.ministone.game.IPlayer;
import fr.ministone.game.effect.*;

public class PlayerTest {
	private IPlayer player1, player2;
	
	private CardMinion card1, card2;
	private CardSpell card3;

	@BeforeEach
	public void init() {
		Set<String> capacities1;
		Set<String> capacities2;

		player1 = new MPlayer("warrior", 10);
		player2 = new MPlayer("mage", 10);
		player1.setOpponent(player2);

		capacities1 = new HashSet<String>();
		capacities2 = new HashSet<String>();
		capacities1.add("charge");
		capacities2.add("provocation");

		card1 = new CardMinion("1", "shared", player1, "Card Minion 1", 1, 1, 10, capacities1, new HashMap<String,Integer>());
		card2 = new CardMinion("2", "shared", player2, "Card Minion 2", 1, 1, 10, capacities2, new HashMap<String,Integer>());
		card3 = new CardSpell("3", "shared", player1, "Card Spell 3", 1, new HashSet<SingleTargetEffect>(), new HashSet<MultipleTargetEffect>(), new HashSet<GlobalEffect>());

		player1.getDeck().add(card1);
		player2.getDeck().add(card2);

		player1.getBoard().put(card1.getId(), card1);
		player2.getBoard().put(card2.getId(), card2);
	}

	@Test 
	public void testInit() {
		assertNotEquals(null, player1.getHero());
		assertNotEquals(null, player2.getHero());
	}
	@Test
	public void testDrawCard() {
		assertEquals(0, player1.getHand().size());
		player1.drawCard(false);
		assertEquals(1, player1.getHand().size());
	}

	@Test
	public void testPlayMinion() {
		Card cardDrawn = player1.drawCard(false);
		assertEquals(1, player1.getHand().size());
		assertEquals(1, player1.getBoard().size());

		player1.summonMinion(cardDrawn.getId());
		assertEquals(0, player1.getHand().size());
		assertEquals(2, player1.getBoard().size());
	}

	@Test
	public void testPlaySpell() {
		assertEquals(0, player1.getHand().size());
		player1.getHand().put(card3.getId(), card3);
		assertEquals(1, player1.getHand().size());

		player1.castSpell(card3.getId());

		assertEquals(0, player1.getHand().size());
	}

	@Test
	public void testHeroSpecial() {
		assertEquals(0, player1.getHero().getArmor());
		player1.heroSpecial();
		assertEquals(2, player1.getHero().getArmor());

		player2.heroSpecial(false, "hero");
		assertEquals(0, player1.getHero().getArmor());
		assertEquals(Constants.HEROHEALTHMAX, player1.getHero().getHealth());
	}

	@Test
	public void testSetOpponent() {
		player1.setOpponent(player2);

		assertEquals(player2, player1.getOpponent());
		assertEquals(player1, player2.getOpponent());
	}

	@Test 
	public void testAttackMinion() {
		assertEquals(10, card1.getHealth());
		assertEquals(10, card2.getHealth());
		player1.attack(card1.getId(), card2.getId());

		assertEquals(9, card1.getHealth());
		assertEquals(9, card2.getHealth());
	}

	@Test
	public void testAttackHero() {
		assertEquals(Constants.HEROHEALTHMAX, player2.getHero().getHealth());

		player1.attack(card1.getId(), "hero");

		assertEquals(29, player2.getHero().getHealth());
	}
}