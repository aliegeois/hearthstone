package game.hero;

import game.Entity;
import game.Player;

public class HeroWarrior extends Hero {
	public HeroWarrior(Player player) {
		super(player);
	}
	
	@Override
	public void special() {
		boostArmor(2);
	}

	@Override
	public void special(Entity e){
		
	}
}
