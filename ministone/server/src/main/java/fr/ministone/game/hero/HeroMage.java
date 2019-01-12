package fr.ministone.game.hero;

import fr.ministone.game.IEntity;
import fr.ministone.game.Player;

public class HeroMage extends Hero {
	public HeroMage(Player player) {
		super(player);
	}
	
	@Override
	public void special(IEntity e) {
		e.takeDamage(2);
	}

	@Override
	public void special(){
		
	}
}
