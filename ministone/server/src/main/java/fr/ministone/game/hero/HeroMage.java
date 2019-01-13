package fr.ministone.game.hero;

import fr.ministone.game.IEntity;
import fr.ministone.game.IPlayer;

public class HeroMage extends Hero {
	public HeroMage(IPlayer player) {
		super(player);
	}
	
	@Override
	public void special(IEntity e) {
		e.takeDamage(2);
	}

	@Override
	public void special() {
		throw new UnsupportedOperationException();
	}
}
