package fr.ministone.game.hero;

//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.UUID;

//import fr.ministone.game.card.CardMinion;
import fr.ministone.game.IEntity;

public class HeroPaladin extends Hero {
	@Override
	public void special() {
		//player.summonMinion(new CardMinion(UUID.randomUUID().getLeastSignificantBits(), "paladin", player, "Recrue de la Main d'argent", 1, 1, 1, new HashSet<>(), new HashMap<>()));
		player.summonMinion("Recrue de la Main d'argent");
	}

	@Override
	public void special(IEntity e) {
		throw new UnsupportedOperationException();
	}
}
