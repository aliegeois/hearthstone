package game;

public interface GameEvent {
	void playMinion(String playerName, String cardId);

	void attackMinion(String playerName, String minionId1, String minionId2);

	void useSpell(String playerName, String cardId);

	void heroSpecial(String playerName, Entity target);

	void endTurn(String playerName);
}
