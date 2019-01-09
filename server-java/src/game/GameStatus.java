package game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import game.hero.Hero;

public class GameStatus { //Cette classe est renvoyée au client pour l'informer des modifications de la partie engendrées par l'action utilisateur
	
	private Hero ownHero;
	private Hero opponentHero;
	
	private Map<Integer, CardMinion> ownBoard = new HashMap<>();
	private Map<Integer, CardMinion> opponentBoard = new HashMap<>();
	
	private Set<Card> ownDeck = new HashSet<>();
	private int numberCardOpponentDeck;
	
	private Map<Integer, Card> ownHand = new HashMap<>();
	private int numberCardOpponentHand;
	
	GameStatus(Game game, String playerName) {
		Player player = game.getPlayer(playerName);
		Player opponent = player.getOpponent();
		
		this.ownHero = player.getHero();
		this.opponentHero = opponent.getHero();
		
		this.ownBoard = player.getBoard();
		this.opponentBoard = opponent.getBoard();
		
		this.ownDeck = player.getDeck();
		this.numberCardOpponentDeck = opponent.getDeck().size();
		
		this.ownHand = player.getHand();
		this.numberCardOpponentHand = opponent.getHand().size();

	}
	
	
	
	

}
