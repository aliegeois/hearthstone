package fr.ministone.message;

public class MessageGameCreated {
	// Nom du joueur qui fait la demande d'affrontement et de celui qui se fait affronter
	private String userAsking, userAsked;

	public MessageGameCreated() {}

	public MessageGameCreated(String userAsking, String userAsked) {
		this.userAsking = userAsking;
		this.userAsked = userAsked;
	}

	public String getUserAsking() {
		return userAsking;
	}

	public String getUserAsked() {
		return userAsked;
	}

	public void setUserAsking(String newAsking) {
		userAsking = newAsking;
	}

	public void setUserAsked(String newAsked) {
		userAsked = newAsked;
	}
}