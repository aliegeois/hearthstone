package fr.ministone.game.hero;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import fr.ministone.game.card.CardMinion;
import fr.ministone.game.IEntity;

public class HeroPaladin extends Hero {
	@Override
	public void special() {
		if(player.looseMana(1)) {
			Long cardId = UUID.randomUUID().getLeastSignificantBits(); // Valeur temporaire
			CardMinion minion = new CardMinion(cardId, "paladin", player, "SilverHand recruit", 1, 1, 1, new HashSet<String>(), new HashMap<String,Integer>());

			player.getBoard().put(cardId, minion);
		}
	}

	@Override
	public void special(IEntity e) {
		throw new UnsupportedOperationException();
	}
}
