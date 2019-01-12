package fr.ministone.game.hero;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import fr.ministone.game.CardMinion;
import fr.ministone.game.IEntity;
import fr.ministone.game.Player;

public class HeroPaladin extends Hero {
	public HeroPaladin(Player player) {
		super(player);
	}
	
	@Override
	public void special() {
		
		String idCarte = UUID.randomUUID().toString(); //valeur temp
		Set<String> cap = new HashSet<String>();
		Map<String,Integer> boost = new HashMap<String,Integer>();
		CardMinion carte = new CardMinion(idCarte, player, "SilverHand recruit", 1, 1, 1, cap, boost);
		player.getHand().put(idCarte, carte);
		player.playMinion(idCarte);
	}

	@Override
	public void special(IEntity e){

	}
}
