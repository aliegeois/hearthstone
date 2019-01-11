package game.effect;

import game.CardSpell;
import game.Entity;

public class Transform extends SingleTargetEffect{
	private Entity into;
	
	public Transform(CardSpell card, Entity into) {
		super(card);
		this.into = into;		
	}
	
	@Override
	public void play() {
		throw new UnsupportedOperationException();
	}
	
	//TODO : voir pourquoi ça ne marche pas alors que c'est sensé être la base de l'héritage
	public void play(Entity target) {
		target = into;
	}
}
