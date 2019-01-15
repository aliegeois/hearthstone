package fr.ministone.game;

public interface IPlayerMessageSender {
    public void sendSummonMinion(Long minionId);
	public void sendAttack(boolean hero, Long cardId, Long targetId);
	
    public void sendCastTargetedSpell(boolean own, boolean hero, Long spellId, Long targetId);
    public void sendCastUntargetedSpell(Long spellId);

    public void sendHeroTargetedSpecial(boolean own, boolean hero, Long targetId);
    public void sendHeroUntargetedSpecial();

    public void sendNextTurn(String cardName, Long cardId, String cardType);
    public void sendTimeout();

    public void sendDrawCard(String cardName, Long cardId, String cardType);

    public void sendVictory();
}