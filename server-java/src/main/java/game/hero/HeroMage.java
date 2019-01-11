package game.hero;

import game.Entity;
import game.Player;

public class HeroMage extends Hero {
	public HeroMage(Player player) {
		super(player);
	}
	
	@Override
	public void special(Entity e) {
		e.takeDamage(2);
	}

	@Override
	public void special(){
		
	}
}
