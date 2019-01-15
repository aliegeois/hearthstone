package fr.ministone.game;

import java.util.Map;
import java.util.Set;

import fr.ministone.game.card.*;
import fr.ministone.game.hero.Hero;

public interface IPlayer {
    public void setOpponent(IPlayer p);

	public void summonMinion(Long minionId);
	public void summonMinion(CardMinion minion);
	public void summonMinion(String minionName);
    public void attack(boolean hero, Long cardId, Long targetId);

	public Card drawCard(boolean send);
	public Card drawCard(Card card, boolean send);

    public void castSpell(boolean own, boolean hero, Long cardId, Long targetId);
    public void castSpell(Long cardId);

    public void heroSpecial(boolean own, boolean hero, Long targetId);
    public void heroSpecial();

    public void nextTurn();

    public String getName();
    public String getSessionId();
    public Set<Card> getDeck();
    public Map<Long, Card> getHand();
    public Map<Long, CardMinion> getBoard();
    public Hero getHero();
    public IPlayer getOpponent();
    public int getManaMax();
    public int getMana();

    public boolean looseMana(int quantity);

	public boolean checkDead();

	public CardMinion findMinionByName(String name);
}