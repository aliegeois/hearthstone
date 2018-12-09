package server;

public class User {
	// Nom de l'utilisateur (unique)
	private String name;
	// ignore
	private String sessionId;
	// Nombre de victoires et de défaites
	private int nbWins, nbLoses;
	// Indique si l'utilisateur est en train de créer une partie
	private boolean inTemporaryGame;

	public User(String name, String sessionId) {
		this.name = name == null ? "" : name; // Putain de bordel de merde
		this.sessionId = sessionId;
		this.inTemporaryGame = false;
		this.nbWins = 0;
		this.nbLoses = 0;
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

	public boolean isInTemporaryGame() {
		return inTemporaryGame;
	}

	public int getNbLoses() {
		return nbLoses;
	}

	public void setInTemporaryGame(boolean newInTemporaryGame) {
		inTemporaryGame = newInTemporaryGame;
	}
}