package game.hero;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import game.CardMinion;
import game.Player;

public class HeroPaladin extends Hero {
	public HeroPaladin(Player player) {
		super(player);
	}
	
	public void special() {
		
		//TODO: initialiser la valeur de idCarte à celle de la carte correspondante
		int idCarte = 0; //valeur temp
		Set<String> cap = Collections.emptySet();
		Map<String,Integer> boost = Collections.emptyMap();
		CardMinion carte = new CardMinion(idCarte, player, "SilverHand recruit", 1, 1, 1, cap, boost);
		player.getHand().put(idCarte, carte);
		player.playMinion(idCarte);
	}
}
