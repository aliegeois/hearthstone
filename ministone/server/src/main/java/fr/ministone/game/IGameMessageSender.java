package fr.ministone.game;

public interface IGameMessageSender {

    public void sendIsStarting(String playerName);

    //public void sendSummonMinion(String playerName, String cardId);
    //public void sendAttack(String playerName, String cardId, String targetId);

    //public void sendCastTargetedSpell(String playerName, boolean own, String cardId, String targetId);
	//public void sendCastUntargetedSpell(String playerName, String cardId);
	
    //public void sendHeroTargetedSpecial(String playerName, boolean own, String targetId);
    //public void sendHeroUntargetedSpecial(String playerName);

    //public void sendNextTurn(String playerName);
    //public void sendTimeout(String playerName);

    //public void sendDrawCard(String playerName, String cardName, String uuid);

    public void sendVictory(String playerName);
}