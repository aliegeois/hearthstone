package fr.ministone.game;

import fr.ministone.User;

public class MGame extends Game {
	public MGame(User u1, User u2) {
		super("", new MSimpMessagingTemplate(), u1, u2);
	}
}