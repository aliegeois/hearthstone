package fr.ministone.game.cards;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ministone.game.effect.*;
import fr.ministone.game.card.*;
import fr.ministone.game.*;

public class CardSpellTest {
	private CardSpell card;

	private IPlayer player1, player2;
	private CardMinion card1, card2, card3, card4;

	private Set<SingleTargetEffect> ste;
	private Set<MultipleTargetEffect> mte;
	private Set<GlobalEffect> gte;

	@BeforeEach
	private void init() {
		player1 = new MPlayer("mage");
		player2 = new MPlayer("paladin");
		player1.setOpponent(player2);

		card1 = new CardMinion("1", "shared", player1, "Card Minion 1", 1, 1, 1, new HashSet<String>(), new HashMap<String, Integer>());
		card2 = new CardMinion("2", "shared", player1, "Card Minion 2", 1, 1, 3, new HashSet<String>(), new HashMap<String, Integer>());
		card3 = new CardMinion("3", "shared", player2, "Card Minion 3", 1, 1, 1, new HashSet<String>(), new HashMap<String, Integer>());
		card4 = new CardMinion("4", "shared", player2, "Card Minion 4", 1, 1, 3, new HashSet<String>(), new HashMap<String, Integer>());

		player1.getDeck().add(card1);
		player1.getDeck().add(card2);
		player2.getDeck().add(card3);
		player2.getDeck().add(card4);

		player1.getBoard().put(card1.getId(), card1);
		player1.getBoard().put(card2.getId(), card2);
		player2.getBoard().put(card3.getId(), card3);
		player2.getBoard().put(card4.getId(), card4);

		ste = new HashSet<SingleTargetEffect>();
		mte = new HashSet<MultipleTargetEffect>();
		gte = new HashSet<GlobalEffect>();
	}

	@Test
	public void testCardSpellDamage() {
		SingleTargetDamage effect1 = new SingleTargetDamage(3);
		MultipleTargetDamage effect2 = new MultipleTargetDamage(true, true, false, false, 1);
		ste.add(effect1);
		mte.add(effect2);
		card = new CardSpell("0", "shared", player1, "Test", 1, ste, mte, gte);

		player1.getHand().put(card.getId(), card);

		assertEquals(2, player1.getBoard().size());
		assertEquals(2, player2.getBoard().size());

		card.play(player2.getHero());

		player1.checkDead();
		player2.checkDead();

		assertEquals(27, player2.getHero().getHealth());
		assertEquals(1, player1.getBoard().size());
		assertEquals(1, player2.getBoard().size());
	}

	@Test
	public void testCardSpellHeal() {
		SingleTargetHeal effect1 = new SingleTargetHeal(3);
		MultipleTargetHeal effect2 = new MultipleTargetHeal(true, true, false, false, 1);
		ste.add(effect1);
		mte.add(effect2);
		card = new CardSpell("0", "shared", player1, "test", 1, ste, mte, gte);

		player1.getHand().put(card.getId(), card);
		player1.getHero().takeDamage(4);
		card2.takeDamage(2);
		assertEquals(26, player1.getHero().getHealth());
		assertEquals(1, card2.getHealth());

		card.play(player1.getHero());

		assertEquals(29, player1.getHero().getHealth());
		assertEquals(1, card1.getHealth());
		assertEquals(2, card2.getHealth());
	}

	@Test 
	public void testCardSpellTransform() {
		/*CardMinion mouton = new CardMinion(card3.getId(), "shared", card1.getOwner(), "mouton", 0, 0, 1, new HashSet<String>(), new HashMap<String, Integer>());
		Transform effect = new Transform(mouton);
		ste.add(effect);
		card = new CardSpell("0", "shared", player1, "test", 1, ste, mte, gte);

		player1.getHand().put(card.getId(), card);
		player1.getBoard().put(card1.getId(), card1);

		assertEquals("Card Minion 1", card1.getName());

		card.play(card1);

		assertEquals("mouton", card1.getName());*/
	}

	@Test
	public void testDraw() {
		DrawRandom effect = new DrawRandom(2);
		gte.add(effect);
		card = new CardSpell("0", "shared", player1, "test", 1, ste, mte, gte);

		player1.getHand().put(card.getId(), card);
		
		assertEquals(1, player1.getHand().size());

		card.play();

		assertEquals(3, player1.getHand().size());
	}
}