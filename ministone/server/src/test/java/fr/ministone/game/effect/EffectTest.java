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

	private Set<SingleTargetEffect> ste;
	private Set<MultipleTargetEffect> mte;
	private Set<GlobalEffect> gte;

	@BeforeEach
	public void init() {
		player1 = new PlayerMock("warrior");
		player2 = new PlayerMock("mage");
		player1.setOpponent(player2);

		card1 = new CardMinion(1l, "shared", player1, "Card Minion 1", 1, 1, 1, new HashSet<String>(), new HashMap<String, Integer>());
		card2 = new CardMinion(2l, "shared", player1, "Card Minion 2", 1, 1, 3, new HashSet<String>(), new HashMap<String, Integer>());
		card3 = new CardMinion(3l, "shared", player2, "Card Minion 3", 1, 1, 1, new HashSet<String>(), new HashMap<String, Integer>());
		card4 = new CardMinion(4l, "shared", player2, "Card Minion 4", 1, 1, 3, new HashSet<String>(), new HashMap<String, Integer>());

		player1.getDeck().add(card1);
		player1.getDeck().add(card2);
		player2.getDeck().add(card3);
		player2.getDeck().add(card4);

		ste = new HashSet<SingleTargetEffect>();
		mte = new HashSet<MultipleTargetEffect>();
		gte = new HashSet<GlobalEffect>();
	}

	@Test
	void testDrawRandom() {
		DrawRandom effect = new DrawRandom(2);
		gte.add(effect);
		CardSpell card = new CardSpell(0l, "shared", player1, "Sprint", 7, ste, mte, gte);
		
		assertEquals(0, player1.getHand().size());
		assertEquals(0, player1.getBoard().size());
		assertEquals(2, player1.getDeck().size());

		player1.getHand().put(card.getId(), card);

		assertEquals(1, player1.getHand().size());

		effect.play();

		assertEquals(3, player1.getHand().size());
	}

	@Test
	void testMultTarBuffOwnBoard() {
		MultipleTargetBuff effect = new MultipleTargetBuff(true, false, false, false, 2, 0);
		mte.add(effect);
		CardSpell card = new CardSpell(0l, "shared", player1, "Sprint", 7, ste, mte, gte);

		player1.getHand().put(card.getId(), card);
		player1.getBoard().put(card1.getId(), card1);
		player1.getBoard().put(card2.getId(), card2);

		effect.play();
	 
		assertEquals(3, card1.getHealth());
		assertEquals(5, card2.getHealth());
	}

	@Test
	void testMultTarBuffOpponentBoard() {
		MultipleTargetBuff effect = new MultipleTargetBuff(false, true, false, false, 0, 2);
		mte.add(effect);
		CardSpell card = new CardSpell(0l, "shared", player1, "Sprint", 7, ste, mte, gte);

		player1.getHand().put(card.getId(), card);
		player2.getBoard().put(card3.getId(), card3);
		player2.getBoard().put(card4.getId(), card4);

		effect.play();

		assertEquals(3, card3.getDamage());
		assertEquals(3, card4.getDamage());
	}

	@Test
	void testMultTarDamageAll() {
		MultipleTargetDamage effect = new MultipleTargetDamage(true, true, true, true, 2);
		mte.add(effect);
		CardSpell card = new CardSpell(0l, "shared", player1, "Sprint", 7, ste, mte, gte);

		assertEquals(0, player1.getBoard().size());
		assertEquals(0, player2.getBoard().size());

		player1.getHand().put(card.getId(), card);
		player1.getBoard().put(card1.getId(), card1);
		player1.getBoard().put(card2.getId(), card2);
		player2.getBoard().put(card3.getId(), card3);
		player2.getBoard().put(card4.getId(), card4);

		assertEquals(2, player1.getBoard().size());
		assertEquals(2, player2.getBoard().size());

		effect.play();
		player1.checkDead();
		player2.checkDead();
		
		assertEquals(Constants.HEROHEALTHMAX - 2, player1.getHero().getHealth());
		assertEquals(Constants.HEROHEALTHMAX - 2, player1.getHero().getHealth());

		assertEquals(1, player1.getBoard().size());
		assertEquals(1, player2.getBoard().size()); 
	}

	@Test 
	void testMultTarHealBothHeroes() {
		MultipleTargetHeal effect = new MultipleTargetHeal(false, false, true, true, 5);
		mte.add(effect);
		CardSpell card = new CardSpell(0l, "shared", player1, "Sprint", 7, ste, mte, gte);

		player1.getHand().put(card.getId(), card);
		player1.getHero().takeDamage(2);
		player2.getHero().takeDamage(8);

		assertEquals(28, player1.getHero().getHealth());
		assertEquals(22, player2.getHero().getHealth());

		effect.play();

		assertEquals(Constants.HEROHEALTHMAX, player1.getHero().getHealth());
		assertEquals(27, player2.getHero().getHealth());
	}

	@Test
	void testSinglTarDamBuff() {
		SingleTargetDamageBuff effect = new SingleTargetDamageBuff(1);
		ste.add(effect);
		CardSpell card = new CardSpell(0l, "shared", player1, "Sprint", 7, ste, mte, gte);

		player1.getHand().put(card.getId(), card);
		player1.getBoard().put(card1.getId(), card1);

		assertEquals(1, card1.getDamage());

		effect.play(card1);

		assertEquals(2, card1.getDamage());
	}

	@Test
	void testSinglTarHealthBuff() {
		SingleTargetLifeBuff effect = new SingleTargetLifeBuff(1);
		ste.add(effect);
		CardSpell card = new CardSpell(0l, "shared", player1, "Sprint", 7, ste, mte, gte);

		player1.getHand().put(card.getId(), card);
		player1.getBoard().put(card3.getId(), card3);

		assertEquals(1, card3.getHealth());

		effect.play(card3);

		assertEquals(2, card3.getHealth());
	}

	@Test
	void testSinglTargDamage() {
		SingleTargetDamage effect = new SingleTargetDamage(10);
		
		assertEquals(Constants.HEROHEALTHMAX, player1.getHero().getHealth());

		effect.play(player2.getHero());

		assertEquals(Constants.HEROHEALTHMAX - 10, player2.getHero().getHealth());
	}

	@Test
	void testTransform() {
		/*CardMinion mouton = new CardMinion(card3.getId(), "shared", player2, "mouton", 0, 0, 1, new HashSet<String>(), new HashMap<String, Integer>()); // Doute sur card3.getId()
		Transform effect = new Transform(mouton);
		
		player2.getBoard().put(card3.getId(), card3);

		effect.play(card3);

		assertEquals("mouton", player2.getBoard().get("3").getName());*/
	}

	void testDrawSpecific() {
		// TODO
	}
}