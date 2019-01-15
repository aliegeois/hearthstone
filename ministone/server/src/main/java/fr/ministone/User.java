package fr.ministone;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	// Nom de l'utilisateur (unique)
	
	private String name;
	
	
	@Transient
	private String sessionId;
	// Nombre de victoires et de défaites
	private int nbWins, nbLoses;
	// Niveau (novice, regular, expert)
	private String level;
	// Type de héro (paladin, mage, warrior)
	@Transient
	private String heroType;
	
	@Transient
	private String temporaryGameId;

	@Transient
	private User opponent;

	public User() {}

	public User(String name, String sessionId) {
		this.name = name == null ? "" : name; // Putain de bordel de merde
		this.sessionId = sessionId;
		this.temporaryGameId = null;
		this.nbWins = 0;
		this.nbLoses = 0;
		this.opponent = null;
		this.level = "regular";
		this.heroType = "warrior";
	}

	public User(String name, String sessionId, String level, String heroType) {
		this.name = name == null ? "" : name; // Putain de bordel de merde
		this.sessionId = sessionId;
		this.temporaryGameId = null;
		this.nbWins = 0;
		this.nbLoses = 0;
		this.opponent = null;
		this.level = level;
		this.heroType = heroType;
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

	public String getTemporaryGameId() {
		return temporaryGameId;
	}

	public int getNbLoses() {
		return nbLoses;
	}

	public String getLevel() {
		return level;
	}

	public String getHeroType() {
		return heroType;
	}

	public User getOpponent() {
		return opponent;
	}

	public void setTemporaryGameId(String newTemporaryGameId) {
		temporaryGameId = newTemporaryGameId;
	}

	public void setOpponent(User op) {
		opponent = op;
	}

	public void setLevel(String lvl) {
		level = lvl;
	}

	public void setHeroType(String ht) {
		heroType = ht;
	}
}