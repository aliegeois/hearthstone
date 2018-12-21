package fr.ministone.server;

import java.util.UUID;

public class User {
	// Nom de l'utilisateur (unique)
	private String name;
	// ignore
	private String sessionId;
	// Nombre de victoires et de défaites
	private int nbWins, nbLoses;
	
	private UUID temporaryGameId;

	private User opponent;

	public User(String name, String sessionId) {
		this.name = name == null ? "" : name; // Putain de bordel de merde
		this.sessionId = sessionId;
		this.temporaryGameId = null;
		this.nbWins = 0;
		this.nbLoses = 0;
		this.opponent = null;
	}

	public String getName() {
		return name;
	}

	public String getSessionId() {
		return sessionId;
	}

	public int getNbWins() {
		return nbWins;
	}

	public UUID getTemporaryGameId() {
		return temporaryGameId;
	}

	public int getNbLoses() {
		return nbLoses;
	}

	public User getOpponent() {
		return opponent;
	}

	public void setTemporaryGameId(UUID newTemporaryGameId) {
		temporaryGameId = newTemporaryGameId;
	}

	public void setOpponent(User op) {
		opponent = op;
	}
}
