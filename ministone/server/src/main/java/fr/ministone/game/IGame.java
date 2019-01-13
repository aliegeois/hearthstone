package fr.ministone.game;

public interface IGame extends IGameMessageReceiver, IGameMessageSender {
    public void start();

    public boolean containsPlayer(String sessionId);
    public IPlayer getPlayer(String sessionId);

    public IPlayer getPlaying();

    public void checkBoard();
}