package fr.ministone.game;

public interface IGameEvent {
	public void receiveSummonMinion(String playerName, String cardId);

	public void receiveAttack(String playerName, String cardId, String targetId);

	public void receiveCastSpell(String playerName, String cardId);
	public void receiveCastSpell(String playerName, boolean own, String cardId, String targetId);

	public void receiveSpecial(String playerName);
	public void receiveSpecial(String playerName, boolean own, String targetId);

	public void receiveEndTurn(String playerName);
}
