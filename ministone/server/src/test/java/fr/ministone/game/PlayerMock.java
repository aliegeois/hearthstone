package fr.ministone.game;

public class PlayerMock extends Player {
    public PlayerMock(String heroType, int mana) {
        super("name-test", "session-id-test", "game-id-test", heroType);

        this.manaMax = 1;
        this.mana = mana;
        this.template = new MSimpMessagingTemplate();
	}
	
	public PlayerMock(String heroType) {
		this(heroType, 10);
    }
    
    public void setMana(int quantity){
        this.mana = quantity;
    }
}