package fr.ministone.game.hero;

import fr.ministone.game.IEntity;

public class HeroMage extends Hero {
	@Override
	public void special(IEntity e) {
		e.takeDamage(2);
	}

	@Override
	public void special() {
		throw new UnsupportedOperationException();
	}
}
