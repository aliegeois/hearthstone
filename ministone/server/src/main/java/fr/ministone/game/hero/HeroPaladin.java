package fr.ministone.game.hero;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import fr.ministone.game.card.CardMinion;
import fr.ministone.game.IEntity;

public class HeroPaladin extends Hero {
	public HeroPaladin() {
		super();
	}
	
	@Override
	public void special() {
		String cardId = UUID.randomUUID().toString(); // Valeur temporaire
		CardMinion minion = new CardMinion(cardId, "paladin", player, "SilverHand recruit", 1, 1, 1, new HashSet<String>(), new HashMap<String,Integer>());

		player.getHand().put(cardId, minion);
		player.summonMinion(cardId);
	}

	@Override
	public void special(IEntity e) {
		throw new UnsupportedOperationException();
	}
}
