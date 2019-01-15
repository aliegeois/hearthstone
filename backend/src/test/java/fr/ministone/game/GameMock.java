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

public class GameMock implements IGame, IGameMessageReceiver, IGameMessageSender {
	private Map<String, IPlayer> players = new HashMap<>();
	private IPlayer playing;
	private CardMinionRepository minionRepository = new MCardMinionRepository();
	private CardSpellRepository spellRepository = new MCardSpellRepository();
	
	public GameMock(User user1, User user2) {
		PlayerMock player1 = new PlayerMock("warrior");
		PlayerMock player2 = new PlayerMock("paladin");

		CardMinion carte1 = new CardMinion(1l, "warrior", player1, "nom-test-carte1", 1, 1, 1, new HashSet<String>(), new HashMap<String, Integer>());
		CardSpell carte2 = new CardSpell(2l, "paladin", player2, "nom-test-carte2", 1, new HashSet<SingleTargetEffect>(), new HashSet<MultipleTargetEffect>(), new HashSet<GlobalEffect>());

		minionRepository.save(carte1);
		spellRepository.save(carte2);

		player1.setCardMinionRepository(minionRepository);
		player2.setCardSpellRepository(spellRepository);

		player1.setOpponent(player2);
		this.players.put(user1.getName(), player1);
		this.players.put(user2.getName(), player2);
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
		//sendNextTurn(playing.getName());
		//sendIsStarting(playing.getName());
	}

	@Override
	public void receiveSummonMinion(String playerName, String cardId) {
		
	}

	//@Override
	public void receiveAttack(String playerName, String cardId, String targetId) {
		
	}

	@Override
	public void receiveCastSpell(String playerName, String cardId) {
		
	}

	//@Override
	public void receiveCastSpell(String playerName, boolean own, String cardId, String targetId) {
		
	}

	@Override
	public void receiveHeroSpecial(String playerName) {
	
	}

	//@Override
	public void receiveHeroSpecial(String playerName, boolean own, String targetId) {
	
	}

	@Override
	public void receiveEndTurn(String playerName) { // À faire
		
	}
	
	/*@Override
	public void sendSetHero(String playerName, String heroType) {
	
	}

	@Override
    public void sendSetOpponentHero(String playerName, String heroType) {
		
	}

	@Override
	public void sendIsStarting(String playerName) {
		
	}*/

	@Override
    public void sendVictory(String playerName) {
		
	}


    @Override
	public boolean containsPlayer(String sessionId) {
		return getPlayer(sessionId) != null;
	}

    @Override
	public IPlayer getPlayer(String sessionId) {
		for(IPlayer p : players.values())
			if(sessionId.equals(p.getSessionId()))
				return p;
		return null;
	}
    
    @Override
	public IPlayer getPlaying() {
		return playing;
	}
    
    @Override
	public boolean checkBoard() {
		/*for(IPlayer p : players.values()) {
			p.checkDead();
		}*/
		return false;
	}

	@Override
	public void receiveAttack(String playerName, boolean isHero, String cardId, String targetId) {

	}

	@Override
	public void receiveCastSpell(String playerName, boolean own, boolean isHero, String cardId, String targetId) {

	}

	@Override
	public void receiveHeroSpecial(String playerName, boolean own, boolean isHero, String targetId) {

	}

	@Override
	public void receiveConfirmStart(String playerName) {

	}


}
