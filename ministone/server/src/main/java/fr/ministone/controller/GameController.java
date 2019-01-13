package fr.ministone.controller;

import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.messaging.handler.annotation.Header;

import fr.ministone.User;
import fr.ministone.game.Game;
import fr.ministone.game.IPlayer;
import fr.ministone.message.*;
import java.util.Map;
import java.util.HashMap;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
public class GameController {
	// Pour envoyer des messages sans utiliser "@SendTo"
	private SimpMessagingTemplate template;
	// Liste des parties en cours
	private Map<String, Game> games = new HashMap<>();

	@Autowired
	public GameController(SimpMessagingTemplate template) {
		this.template = template;
	}

	// SITE DE LA VIE: https://www.programcreek.com/java-api-examples/?api=org.springframework.messaging.handler.annotation.MessageMapping

	@MessageMapping("/game/{gameId}/setHero")
	public void setHero(@Header("simpSessionId") String sessionId, @DestinationVariable("gameId") String gameId, @Payload MessageSetHero message) {
		Game g = games.get(gameId);
		if(g == null)
			return;
		
		IPlayer p = g.getPlayer(sessionId);
		if(p == null)
			return;
		
		g.receiveSetHero(p.getName(), message.getHeroType());
	}

	@MessageMapping("/game/{gameId}/summonMinion")
	public void summonMinion(@Header("simpSessionId") String sessionId, @DestinationVariable("gameId") String gameId, @Payload MessageSummonMinion message) {
		Game g = games.get(gameId);
		if(g == null)
			return;
		
		IPlayer p = g.getPlayer(sessionId);
		if(p == null)
			return;
		
		g.receiveSummonMinion(p.getName(), message.getCardId());
	}

	@MessageMapping("/game/{gameId}/attack")
	public void attack(@Header("simpSessionId") String sessionId, @DestinationVariable("gameId") String gameId, @Payload MessageAttack message) {
		Game g = games.get(gameId);
		if(g == null)
			return;
		
		IPlayer p = g.getPlayer(sessionId);
		if(p == null)
			return;
		
		g.receiveAttack(p.getName(), message.getCardId(), message.getTargetId());
	}

	@MessageMapping("/game/{gameId}/castUntargetedSpell")
	public void castUntargetedSpell(@Header("simpSessionId") String sessionId, @DestinationVariable("gameId") String gameId, @Payload MessageCastUntargetedSpell message) {
		Game g = games.get(gameId);
		if(g == null)
			return;
		
		IPlayer p = g.getPlayer(sessionId);
		if(p == null)
			return;
		
		g.receiveCastSpell(p.getName(), message.getCardId());
	}

	@MessageMapping("/game/{gameId}/castTargetedSpell")
	public void castTargetedSpell(@Header("simpSessionId") String sessionId, @DestinationVariable("gameId") String gameId, @Payload MessageCastTargetedSpell message) {
		Game g = games.get(gameId);
		if(g == null)
			return;
		
		IPlayer p = g.getPlayer(sessionId);
		if(p == null)
			return;
		
		g.receiveCastSpell(p.getName(), message.isOwn(), message.getCardId(), message.getTargetId());
	}

	@MessageMapping("/game/{gameId}/heroUntargetedSpecial")
	public void heroUntargetedSpecial(@Header("simpSessionId") String sessionId, @DestinationVariable("gameId") String gameId) {
		Game g = games.get(gameId);
		if(g == null)
			return;
		
		IPlayer p = g.getPlayer(sessionId);
		if(p == null)
			return;
		
		g.receiveHeroSpecial(p.getName());
	}

	@MessageMapping("/game/{gameId}/heroTargetedSpecial")
	public void heroTargetedSpecial(@Header("simpSessionId") String sessionId, @DestinationVariable("gameId") String gameId, @Payload MessageHeroTargetedSpecial message) {
		Game g = games.get(gameId);
		if(g == null)
			return;
		
		IPlayer p = g.getPlayer(sessionId);
		if(p == null)
			return;
		
		g.receiveHeroSpecial(p.getName(), message.isOwn(), message.getTargetId());
	}

	@MessageMapping("/game/{gameId}/endTurn")
	public void endTurn(@Header("simpSessionId") String sessionId, @DestinationVariable("gameId") String gameId) {
		Game g = games.get(gameId);
		if(g == null)
			return;
		
		IPlayer p = g.getPlayer(sessionId);
		if(p == null)
			return;
		
		g.receiveEndTurn(p.getName());
	}


	@EventListener
	public void onConnectEvent(SessionConnectEvent event) {
		StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
		System.out.println("onConnect (game): " + headers.getSessionId());
	}

	@EventListener
	public void onDisconnectEvent(SessionDisconnectEvent event) {
		StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
		System.out.println("onDisconnect (game): " + headers.getSessionId());
	}

	public void createGame(String gameId, User player1, User player2) {
		Game g = new Game(gameId, template, player1, player2);
		games.put(gameId, g);
		
		System.out.println("create game with " + player1.getName() + " and " + player2.getName());
	}
}