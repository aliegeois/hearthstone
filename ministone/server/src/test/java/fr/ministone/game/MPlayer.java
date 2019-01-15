package fr.ministone.game;

public class MPlayer extends Player {
    public MPlayer(String heroType, int mana) {
        super("", "", heroType);

        this.manaMax = mana;
        this.mana = mana;
        this.template = new MSimpMessagingTemplate();
	}
	
	public MPlayer(String heroType) {
		this(heroType, 10);
	}
}