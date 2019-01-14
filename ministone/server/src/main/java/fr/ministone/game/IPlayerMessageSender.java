package fr.ministone.game;

public interface IPlayerMessageSender {
    public void sendSummonMinion(String minionId);
	public void sendAttack(String cardId, String targetId);
	
    public void sendCastTargetedSpell(boolean own, String spellId, String targetId);
    public void sendCastUntargetedSpell(String spellId);

    public void sendHeroTargetedSpecial(boolean own, String targetId);
    public void sendHeroUntargetedSpecial();

    public void sendNextTurn(String cardName, String cardId, String cardType);
    public void sendTimeout();

    public void sendDrawCard(String cardName, String cardId, String cardType);

    public void sendVictory();
}