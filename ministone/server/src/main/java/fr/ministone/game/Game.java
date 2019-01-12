package fr.ministone.game;

import fr.ministone.User;
import java.util.UUID;

import org.springframework.messaging.simp.SimpMessagingTemplate;

public class Game implements IGameEvent, IGameMessageSender {
	private SimpMessagingTemplate template;
	private Player player1, player2;
	private Player playing, starting;
	private int turn;
	private UUID id;
	
	Game(UUID id, SimpMessagingTemplate template, User player1, User player2) {
		this.id = id;
		this.template = template;
		this.player1 = new Player(player1.getName(), player1.getSessionId());
		this.player2 = new Player(player2.getName(), player2.getSessionId());
	}

	public Game(Player player1, Player player2) {		
		this.player1 = player1;
		this.player2 = player2;
		
		player1.setOpponent(player2);
		this.turn = 0;
	}
	
	public void addPlayer(Player p) {
		if(player1 == null){
			player1 = p;
		}else if(player2 == null){
			player2 = p;
			player1.setOpponent(player2);
		}	
	}

	public boolean isReady(){
		return((this.player1!=null) && (this.player2 != null));
	}
	
	public void start() {
		double val = Math.random();
		if(val < 0.5){
			playing = this.player1;
		}else{
			playing = this.player2;
		}
		starting = playing;
	}
	
	public void playMinion(Player player, String cardId) {
		player.playMinion(cardId);
	}

	public void attackMinion(Player player, String minionId1, String minionId2) {
		CardMinion minion1 = player.getBoard().get(minionId1);
		CardMinion minion2 = player.getOpponent().getBoard().get(minionId2);
		player.attack(minion1, minion2);
	}

	public void useSpell(Player player, String cardId) {
		player.useSpell(cardId);
	}

	public void heroSpecial(Player player, IEntity target) {
		player.heroSpecial(target);
	}

	public void isStarting() {

	}

    public void summonMinion(String playerName, String cardId) {
		
	}

    public void attack() {

	}

    public void castTargetedSpell() {

	}

    public void castUntargetedSpell() {

	}

    public void targetedSpecial() {

	}

    public void untargetedSpecial() {

	}

    public void timeout() {

	}

    public void drawCard() {

	}

    public void opponentDrawCard() {

	}

    public void win() {

	}

    public void lose() {

	}


	@Override
	public void endTurn(Player player) {
		if(this.playing == player){
			if(playing == player1){
				playing = player2;
			}else{
				playing = player1;
			}
		}
		if(playing == starting){
			turn++;
		}	
	}
	
	public Player getPlaying() {
		return playing;
	}
	
	public int getTurn() {
		return turn;
	}

	public void checkBoard(){
		player1.checkDead();
		player2.checkDead();
	}
}
