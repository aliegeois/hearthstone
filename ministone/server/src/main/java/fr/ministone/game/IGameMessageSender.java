package fr.ministone.game;



public interface IGameMessageSender {
    public void sendIsStarting(String playerName);
    public void sendSummonMinion(String playerName, String cardId);
    public void sendAttack(String playerName, String cardId, String targetId);
    // Si la target est son propre héro, on envoie "ownHero", si héro adverse: "opponentHero", sinon l'id de la carte
    public void sendCastTargetedSpell(String playerName, boolean own, String cardId, String targetId);
    public void sendCastUntargetedSpell(String playerName, String cardId);
    // Si la terget est son propre héro, on envoie "ownHero", si héro adverse: "opponentHero", sinon l'id de la carte
    /*  Si own == true:
            si targetId == "hero":
                Le spécial est dirigé vers notre propre héro
            sinon
                spécial vers une de nos cartes
        Sinon
            si targetid == "hero"
                Le spécial est dirigé vers le héro adverse
            sinon
                spécial vers une carte adverse
        
    */
    public void sendTargetedSpecial(String playerName, boolean own, String targetId);
    public void sendUntargetedSpecial(String playerName);

    public void sendTimeout(String playerName);

    public void sendDrawCard(String playerName, String cardName);
    public void sendOpponentDrawCard(String playerName, String cardName);

    public void sendWin(String playerName);
    public void sendLose(String playerName);
}