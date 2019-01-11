package game.hero;

import game.Entity;
import game.Player;

public class HeroMage extends Hero {
	public HeroMage(Player player) {
		super(player);
	}
	
	public void special(Entity e) {
		e.takeDamage(2);
	}
}
