package fr.ministone.game.hero;

import fr.ministone.game.IEntity;

public class HeroWarrior extends Hero {
	/*public HeroWarrior() {
		super();
	}*/
	
	@Override
	public void special() {
		boostArmor(2);
	}

	@Override
	public void special(IEntity e) {
		throw new UnsupportedOperationException();
	}
}
