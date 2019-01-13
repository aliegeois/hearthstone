package fr.ministone.game;

public interface IGame {

    public void start(IPlayer p1, IPlayer p2);
    public void endTurn();
    public boolean containsPlayer(String sessionId);
    public IPlayer getPlayer(String sessionId);
    public IPlayer getPlaying();
    public int getTurn();
    public void checkBoard();
}