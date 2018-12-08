package server;

import java.nio.channels.NonWritableChannelException;

public class User {
	private String name;
	private int nbWins, nbLoses;
	private boolean connected;

	public User(String name) {
		this.name = name;
		this.connected = true;
		this.nbWins = 0;
		this.nbLoses = 0;
	}

	public String getName() {
		return name;
	}

	public int getNbWins() {
		return nbWins;
	}

	public int getNbLoses() {
		return nbLoses;
	}

	public boolean isConnected() {
		return connected;
	}
}