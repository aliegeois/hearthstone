package fr.ministone.game.hero;

import fr.ministone.game.IEntity;
import fr.ministone.game.IPlayer;

public class HeroWarrior extends Hero {
	public HeroWarrior(IPlayer player) {
		super(player);
	}
	
	@Override
	public void special() {
		boostArmor(2);
	}

	@Override
	public void special(IEntity e) {
		throw new UnsupportedOperationException();
	}
}
