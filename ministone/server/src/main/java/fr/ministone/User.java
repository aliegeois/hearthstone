package fr.ministone;

import java.util.UUID;

public class User {
	// Nom de l'utilisateur (unique)
	private String name;
	// ignore
	private String sessionId;
	// Nombre de victoires et de défaites
	private int nbWins, nbLoses;
	// Niveau (novice, regular, expert)
	private String level;
	
	private UUID temporaryGameId;

	private User opponent;

	public User(String name, String sessionId) {
		this.name = name == null ? "" : name; // Putain de bordel de merde
		this.sessionId = sessionId;
		this.temporaryGameId = null;
		this.nbWins = 0;
		this.nbLoses = 0;
		this.opponent = null;
		this.level = "regular";
	}

	public User(String name, String sessionId, String level) {
		this.name = name == null ? "" : name; // Putain de bordel de merde
		this.sessionId = sessionId;
		this.temporaryGameId = null;
		this.nbWins = 0;
		this.nbLoses = 0;
		this.opponent = null;
		this.level = level;
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

	public String getLevel() {
		return level;
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

	public void setLevel(String lvl) {
		level = lvl;
	}
}