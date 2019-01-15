package fr.ministone.game;

import java.util.Map;
import java.util.Set;

import fr.ministone.game.card.*;
import fr.ministone.game.hero.Hero;

public interface IPlayer {
    public void setOpponent(IPlayer p);

	public void summonMinion(String minionId);
	public void summonMinion(CardMinion minion);
    public void attack(String cardId, String targetId);

	public Card drawCard(boolean send);
	public Card drawCard(Card card, boolean send);

    public void castSpell(boolean own, String cardId, String targetId);
    public void castSpell(String cardId);

    public void heroSpecial(boolean own, String targetId);
    public void heroSpecial();

    public void nextTurn();

    public String getName();
    public String getSessionId();
    public Set<Card> getDeck();
    public Map<String, Card> getHand();
    public Map<String, CardMinion> getBoard();
    public Hero getHero();
    public IPlayer getOpponent();
    public int getManaMax();
    public int getMana();

    public boolean looseMana(int quantity);

	public void checkDead();

	public CardMinion findMinionByName(String name);
}