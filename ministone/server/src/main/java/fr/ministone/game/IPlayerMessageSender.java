package fr.ministone.game;

public interface IPlayerMessageSender {
    public void sendSummonMinion(Long minionId);
	public void sendAttack(Long cardId, Long targetId);
	
    public void sendCastTargetedSpell(boolean own, Long spellId, Long targetId);
    public void sendCastUntargetedSpell(Long spellId);

    public void sendHeroTargetedSpecial(boolean own, Long targetId);
    public void sendHeroUntargetedSpecial();

    public void sendNextTurn(String cardName, Long cardId, String cardType);
    public void sendTimeout();

    public void sendDrawCard(String cardName, Long cardId, String cardType);

    public void sendVictory();
}