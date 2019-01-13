package fr.ministone.game;

public interface IGameMessageReceiver {
	public void receiveSetHero(String playerName, String heroType);

	public void receiveSummonMinion(String playerName, String cardId);

	public void receiveAttack(String playerName, String cardId, String targetId);

	public void receiveCastSpell(String playerName, String cardId);
	public void receiveCastSpell(String playerName, boolean own, String cardId, String targetId);

	public void receiveHeroSpecial(String playerName);
	public void receiveHeroSpecial(String playerName, boolean own, String targetId);

	public void receiveEndTurn(String playerName);
}
