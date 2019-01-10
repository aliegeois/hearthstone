package game.hero;

import game.Player;

public class HeroWarrior extends Hero {
	public HeroWarrior(Player player) {
		super(player);
	}
	
	public void special() {
		boostArmor(2);
	}
}
