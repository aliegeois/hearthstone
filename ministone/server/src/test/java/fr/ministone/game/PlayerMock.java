package fr.ministone.game;

import fr.ministone.repository.CardMinionRepository;
import fr.ministone.repository.MCardMinionRepository;
import fr.ministone.repository.MCardSpellRepository;

public class PlayerMock extends Player {
    public PlayerMock(String heroType, int mana) {
        super("name-test", "session-id-test", "game-id-test", heroType);

        this.manaMax = 1;
        this.mana = mana;
		this.template = new MSimpMessagingTemplate();
		this.cardMinionRepository = new MCardMinionRepository();
		this.cardSpellRepository = new MCardSpellRepository();
	}
	
	public PlayerMock(String heroType) {
		this(heroType, 10);
    }
    
    public void setMana(int quantity) {
        this.mana = quantity;
    }
    
    public void setCardMinionRepository(CardMinionRepository repository) {
        cardMinionRepository = repository;
    }
}