package fr.ministone;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Game {
	private UUID id;
	Map<String, User> players;

	public Game(UUID id, User player1, User player2) {
		this.id = id;
		this.players = new HashMap<>();
		this.players.put(player1.getName(), player1);
		this.players.put(player2.getName(), player2);
	}
}