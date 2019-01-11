package game;

public interface GameEvent {
	void playMinion(Player player, String cardId);

	void attackMinion(Player player, String minionId1, String minionId2);

	void useSpell(Player player, String cardId);

	void heroSpecial(Player player, Entity target);

	void endTurn(Player player);
}
