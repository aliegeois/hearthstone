package game.hero;

import game.Player;

public class HeroPaladin extends Hero {
	public HeroPaladin(Player player) {
		super(player);
	}
	
	public void special() {
		
		//TODO: initialiser la valeur de idCarte à celle de la carte correspondante
		int idCarte = 0; //valeur temp
		player.playMinion(idCarte);
	}
}
