package main.java.game.hero;

import main.java.game.Entity;
import main.java.game.Player;

public class HeroMage extends Hero {
	public HeroMage(Player player) {
		super(player);
	}
	
	public void special(Entity e) {
		e.takeDamage(2);
	}
}
