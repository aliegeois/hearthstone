package fr.ministone.game.hero;

import fr.ministone.game.IEntity;

public class HeroMage extends Hero {
	@Override
	public void special(IEntity e) {
		if(player.looseMana(1))
			e.takeDamage(2);
	}

	@Override
	public void special() {
		throw new UnsupportedOperationException();
	}
}
