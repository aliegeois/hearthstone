package fr.ministone.game.hero;

import fr.ministone.game.IEntity;
import fr.ministone.game.Player;

public class HeroWarrior extends Hero {
	public HeroWarrior(Player player) {
		super(player);
	}
	
	@Override
	public void special() {
		boostArmor(2);
	}

	@Override
	public void special(IEntity e){
		
	}
}
