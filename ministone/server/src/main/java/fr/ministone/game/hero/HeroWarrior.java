package fr.ministone.game.hero;

import fr.ministone.game.IEntity;

public class HeroWarrior extends Hero {
	/*public HeroWarrior() {
		super();
	}*/
	
	@Override
	public void special() {
		buffArmor(2);
	}

	@Override
	public void special(IEntity e) {
		throw new UnsupportedOperationException();
	}
}
