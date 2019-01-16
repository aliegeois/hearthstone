package fr.ministone.game;

import fr.ministone.User;
import fr.ministone.game.IGame;
import fr.ministone.game.IPlayer;
import fr.ministone.game.card.CardMinion;
import fr.ministone.game.card.CardSpell;
import fr.ministone.game.effect.GlobalEffect;
import fr.ministone.game.effect.MultipleTargetEffect;
import fr.ministone.game.effect.SingleTargetEffect;
import fr.ministone.repository.CardMinionRepository;
import fr.ministone.repository.CardSpellRepository;
import fr.ministone.repository.MCardMinionRepository;
import fr.ministone.repository.MCardSpellRepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.springframework.messaging.core.AbstractMessageSendingTemplate;

public class GameMock extends Game {
	private Map<String, IPlayer> players = new HashMap<>();
	private IPlayer playing;
	private CardMinionRepository minionRepository = new MCardMinionRepository();
	private CardSpellRepository spellRepository = new MCardSpellRepository();
	private AbstractMessageSendingTemplate template; 
	
	public GameMock(User user1, User user2, CardMinionRepository minionRepository, CardSpellRepository spellRepository) {
		super(1l, new MSimpMessagingTemplate(), user1, user2, minionRepository, spellRepository);

		//this.turn = 0;
	}
    
    @Override
	public void start() {
		IPlayer p1 = null, p2 = null;

		for(IPlayer p : players.values()) { 
			if(p1 == null)
				p1 = p;
			else
				p2 = p;
		}

		playing = p1;
		for(int i = 0; i < 3; i++) {
			playing.drawCard(false);
			playing.getOpponent().drawCard(false);
		}
		playing.getOpponent().drawCard(false);
		playing.nextTurn();
	}

	


}
