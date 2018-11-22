package game;

public interface GameEvent {
	void playMinion(String playerName, int cardId);

	void attackMinion(String playerName, int minionId1, int minionId2);

	void useSpell(String playerName, int cardId, CardMinion target);

	void heroSpecial(String playerName, Entity target);

	void endTurn(String playerName);
}
