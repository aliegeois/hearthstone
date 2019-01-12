package fr.ministone.game;



public interface IGameMessageSender {
    public void isStarting(String playerName);
    public void summonMinion(String playerName, String cardId);
    public void attack(String playerName, String cardId, String targetId);
    // Si la terget est son propre héro, on envoie "ownHero", si héro adverse: "opponentHero", sinon l'id de la carte
    public void castTargetedSpell(String playerName, String cardId, String targetId);
    public void castUntargetedSpell(String playerName, String cardId);
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
    public void targetedSpecial(String playerName, boolean own, String targetId);
    public void untargetedSpecial(String playerName);
    public void timeout(String playerName);
    public void drawCard(String playerName, String cardName);
    public void opponentDrawCard(String playerName, String cardName);
    public void win(String playerName);
    public void lose(String playerName);
}