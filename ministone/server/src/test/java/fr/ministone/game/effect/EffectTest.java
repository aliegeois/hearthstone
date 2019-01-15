package fr.ministone.game.effect;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;

import fr.ministone.game.*;
import fr.ministone.game.card.*;
import fr.ministone.game.Constants;

public class EffectTest {
	private IPlayer player1, player2;
	private CardMinion card1, card2, card3, card4;

	private Set<SingleTargetEffect> ste = new HashSet<SingleTargetEffect>();
	private Set<MultipleTargetEffect> mte = new HashSet<MultipleTargetEffect>();
	private Set<GlobalEffect> gte = new HashSet<GlobalEffect>();

	@BeforeEach
	public void init() {
		player1 = new MPlayer("warrior", 10);
		player2 = new MPlayer("mage", 10);
		player1.setOpponent(player2);

		card1 = new CardMinion("1", "shared", player1, "Card Minion 1", 1, 1, 1, new HashSet<String>(), new HashMap<String, Integer>());
		card2 = new CardMinion("2", "shared", player1, "Card Minion 2", 1, 1, 3, new HashSet<String>(), new HashMap<String, Integer>());
		card3 = new CardMinion("3", "shared", player2, "Card Minion 3", 1, 1, 1, new HashSet<String>(), new HashMap<String, Integer>());
		card4 = new CardMinion("4", "shared", player2, "Card Minion 4", 1, 1, 3, new HashSet<String>(), new HashMap<String, Integer>());

		player1.getDeck().add(card1);
		player1.getDeck().add(card2);
		player2.getDeck().add(card3);
		player2.getDeck().add(card4);
	}

	@Test
	void testDrawRandom() {
		Set<GlobalEffect> gte = new HashSet<GlobalEffect>();
		DrawRandom effect = new DrawRandom(2);
		gte.add(effect);
		CardSpell card = new CardSpell("0", "shared", player1, "Sprint", 7, ste, mte, gte);
		
		assertEquals(0, player1.getHand().size());
		assertEquals(0, player1.getBoard().size());
		assertEquals(2, player1.getDeck().size());

		player1.getHand().put(card.getId(), card);

		assertEquals(1, player1.getHand().size());

		player1.castSpell(card.getId());

		assertEquals(2, player1.getHand().size());
	}

	@Test
	void testMultTarBuffOwnBoard() {
		Set<MultipleTargetEffect> mte = new HashSet<MultipleTargetEffect>();
		MultipleTargetBuff effect = new MultipleTargetBuff(true, false, false, false, 2, 0);
		mte.add(effect);
		CardSpell card = new CardSpell("0", "shared", player1, "Sprint", 7, ste, mte, gte);

		player1.getHand().put(card.getId(), card);
		player1.getBoard().put(card1.getId(), card1);
		player1.getBoard().put(card2.getId(), card2);

		player1.castSpell(card.getId());
	 
		assertEquals(3, card1.getHealth());
		assertEquals(5, card2.getHealth());
	}

	@Test
	void testMultTarBuffOpponentBoard() {
		Set<MultipleTargetEffect> mte = new HashSet<MultipleTargetEffect>();
		MultipleTargetBuff effect = new MultipleTargetBuff(false, true, false, false, 0, 2);
		mte.add(effect);
		CardSpell card = new CardSpell("0", "shared", player1, "Sprint", 7, ste, mte, gte);

		player1.getHand().put(card.getId(), card);
		player2.getBoard().put(card3.getId(), card3);
		player2.getBoard().put(card4.getId(), card4);

		//card.play();
		player1.castSpell(card.getId());

		assertEquals(3, card3.getDamage());
		assertEquals(3, card4.getDamage());
	}

	@Test
	void testMultTarDamageAll() {
		Set<MultipleTargetEffect> mte = new HashSet<MultipleTargetEffect>();
		MultipleTargetDamage effect = new MultipleTargetDamage(true, true, true, true, 2);
		mte.add(effect);
		CardSpell card = new CardSpell("0", "shared", player1, "Sprint", 7, ste, mte, gte);

		assertEquals(0, player1.getBoard().size());
		assertEquals(0, player2.getBoard().size());

		player1.getHand().put(card.getId(), card);
		player1.getBoard().put(card1.getId(), card1);
		player1.getBoard().put(card2.getId(), card2);
		player2.getBoard().put(card3.getId(), card3);
		player2.getBoard().put(card4.getId(), card4);

		assertEquals(2, player1.getBoard().size());
		assertEquals(2, player2.getBoard().size());

		player1.castSpell(card.getId());
		player1.checkDead();
		player2.checkDead();
		
		assertEquals(Constants.HEROHEALTHMAX - 2, player1.getHero().getHealth());
		assertEquals(Constants.HEROHEALTHMAX - 2, player1.getHero().getHealth());

		assertEquals(1, player1.getBoard().size());
		assertEquals(1, player2.getBoard().size()); 
	}

	@Test 
	void testMultTarHealBothHeroes() {
		Set<MultipleTargetEffect> mte = new HashSet<MultipleTargetEffect>();
		MultipleTargetHeal effect = new MultipleTargetHeal(false, false, true, true, 5);
		mte.add(effect);
		CardSpell card = new CardSpell("0", "shared", player1, "Sprint", 7, ste, mte, gte);

		player1.getHand().put(card.getId(), card);
		player1.getHero().takeDamage(2);
		player2.getHero().takeDamage(8);

		assertEquals(28, player1.getHero().getHealth());
		assertEquals(22, player2.getHero().getHealth());

		player1.castSpell(card.getId());

		assertEquals(Constants.HEROHEALTHMAX, player1.getHero().getHealth());
		assertEquals(27, player2.getHero().getHealth());
	}

	@Test
	void testSinglTarDamBuff() {
		Set<SingleTargetEffect> ste = new HashSet<SingleTargetEffect>();
		SingleTargetDamageBuff effect = new SingleTargetDamageBuff(1);
		ste.add(effect);
		CardSpell card = new CardSpell("0", "shared", player1, "Sprint", 7, ste, mte, gte);

		player1.getHand().put(card.getId(), card);
		player1.getBoard().put(card1.getId(), card1);

		assertEquals(1, card1.getDamage());

		player1.castSpell(true, card.getId(), card1.getId());

		assertEquals(2, card1.getDamage());
	}

	@Test
	void testSinglTarHealthBuff() {
		Set<SingleTargetEffect> ste = new HashSet<SingleTargetEffect>();
		SingleTargetLifeBuff effect = new SingleTargetLifeBuff(1);
		ste.add(effect);
		CardSpell card = new CardSpell("0", "shared", player1, "Sprint", 7, ste, mte, gte);

		player1.getHand().put(card.getId(), card);
		player1.getBoard().put(card3.getId(), card3);

		assertEquals(1, card3.getHealth());

		player1.castSpell(true, card.getId(), card3.getId());

		assertEquals(2, card3.getHealth());
	}

	@Test
	void testSinglTargDamage() {
		Set<SingleTargetEffect> ste = new HashSet<SingleTargetEffect>();
		SingleTargetDamage effect = new SingleTargetDamage(10);
		ste.add(effect);
		CardSpell card = new CardSpell("0", "shared", player1, "Sprint", 7, ste, mte, gte);

		player1.getHand().put(card.getId(), card);

		assertEquals(Constants.HEROHEALTHMAX, player1.getHero().getHealth());

		player1.castSpell(true, card.getId(), "hero");

		assertEquals(Constants.HEROHEALTHMAX - 10, player1.getHero().getHealth());
	}

	@Test
	void testTransform() {
		Set<SingleTargetEffect> ste = new HashSet<SingleTargetEffect>();
		CardMinion mouton = new CardMinion(card3.getId(), "shared", player2, "mouton", 0, 0, 1, new HashSet<String>(), new HashMap<String, Integer>()); // Doute sur card3.getId()
		Transform effect = new Transform(mouton);
		ste.add(effect);
		CardSpell card = new CardSpell("0", "shared", player1, "test", 7, ste, mte, gte);

		player1.getHand().put(card.getId(), card);
		player2.getBoard().put(card3.getId(), card3);

		player1.castSpell(false, card.getId(), card3.getId());

		assertEquals("mouton", player2.getBoard().get("3").getName());
	}
}