package fr.ministone.game;

public interface IGameMessageReceiver {
	public void receiveConfirmStart(String playerName);

	public void receiveSummonMinion(String playerName, String cardId);
	public void receiveAttack(String playerName, boolean isHero, String cardId, String targetId);

	public void receiveCastSpell(String playerName, String cardId);
	public void receiveCastSpell(String playerName, boolean own, boolean isHero, String cardId, String targetId);

	public void receiveHeroSpecial(String playerName);
	public void receiveHeroSpecial(String playerName, boolean own, boolean isHero, String targetId);

	public void receiveEndTurn(String playerName);
}
