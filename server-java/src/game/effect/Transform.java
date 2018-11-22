package game.effect;

import game.CardMinion;
import game.CardSpell;

public class Transform extends Effect{
	private CardMinion into;
	
	public Transform(CardSpell card, CardMinion into) {
		super(card);
		this.into = into;		
	}
	
	@Override
	public void play() {
		throw new UnsupportedOperationException();
	}
	
	//TODO : voir pourquoi ça ne marche pas alors que c'est sensé être la base de l'héritage
	/*@Override
	public void play(CardMinion target) {
		target = into;
	}*/
}
