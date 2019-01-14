package fr.ministone.game;

public class MPlayer extends Player {
    public MPlayer(String heroType) {
        super("", "", heroType);
        this.manaMax = 10;
        this.mana = 10;
        this.template = new MSimpMessagingTemplate();
    }
}