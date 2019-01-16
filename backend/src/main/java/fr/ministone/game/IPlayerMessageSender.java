package fr.ministone.game;

public interface IPlayerMessageSender {
	public void sendSummonMinionFromHand(String minionId);
	public void sendSummonMinionGlobal(String minionName);
	public void sendAttack(boolean hero, String cardId, String targetId);
	
    public void sendCastTargetedSpell(boolean own, boolean hero, String spellId, String targetId);
    public void sendCastUntargetedSpell(String spellId);

    public void sendHeroTargetedSpecial(boolean own, boolean hero, String targetId);
    public void sendHeroUntargetedSpecial();

    public void sendNextTurn(String cardName, String cardId, String cardType);
    public void sendTimeout();

    public void sendDrawCard(String cardName, String cardId, String cardType);

    public void sendVictory();
}