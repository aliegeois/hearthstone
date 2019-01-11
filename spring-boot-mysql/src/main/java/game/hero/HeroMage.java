package game.hero;

import game.Entite;
import game.Player;

public class HeroMage extends Hero {
	public HeroMage(Player player) {
		super(player);
	}
	
	public void special(Entite e) {
		e.takeDamage(2);
	}
}
