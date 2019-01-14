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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.ministone.User;
import fr.ministone.game.Game;
import fr.ministone.game.IGame;
import fr.ministone.game.IPlayer;
import fr.ministone.message.*;
import java.util.Map;
import java.util.HashMap;
import fr.ministone.repository.*;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
public class GameController {
	// Pour envoyer des messages sans utiliser "@SendTo"
	private SimpMessagingTemplate template;
	// Liste des parties en cours
	private Map<String, IGame> games = new HashMap<>();

	@Autowired
	private CardMinionRepository cardMinionRepository;
	@Autowired
	private CardSpellRepository cardSpellRepository;

	@Autowired
	public GameController(SimpMessagingTemplate template, CardMinionRepository cardMinionRepository, CardSpellRepository cardSpellRepository) {
		this.template = template;
		this.cardMinionRepository = cardMinionRepository;
		this.cardSpellRepository = cardSpellRepository;
	}

	// SITE DE LA VIE: https://www.programcreek.com/java-api-examples/?api=org.springframework.messaging.handler.annotation.MessageMapping

	@MessageMapping("/game/{gameId}/summonMinion")
	public void summonMinion(@Header("simpSessionId") String sessionId, @DestinationVariable("gameId") String gameId, @Payload MessageSummonMinion message) {
		IGame g = games.get(gameId);
		if(g == null)
			return;
		
		IPlayer p = g.getPlayer(sessionId);
		if(p == null)
			return;
		
		g.receiveSummonMinion(p.getName(), message.getCardId());
	}

	@MessageMapping("/game/{gameId}/attack")
	public void attack(@Header("simpSessionId") String sessionId, @DestinationVariable("gameId") String gameId, @Payload MessageAttack message) {
		IGame g = games.get(gameId);
		if(g == null)
			return;
		
		IPlayer p = g.getPlayer(sessionId);
		if(p == null)
			return;
		
		g.receiveAttack(p.getName(), message.getCardId(), message.getTargetId());
	}

	@MessageMapping("/game/{gameId}/castUntargetedSpell")
	public void castUntargetedSpell(@Header("simpSessionId") String sessionId, @DestinationVariable("gameId") String gameId, @Payload MessageCastUntargetedSpell message) {
		IGame g = games.get(gameId);
		if(g == null)
			return;
		
		IPlayer p = g.getPlayer(sessionId);
		if(p == null)
			return;
		
		g.receiveCastSpell(p.getName(), message.getCardId());
	}

	@MessageMapping("/game/{gameId}/castTargetedSpell")
	public void castTargetedSpell(@Header("simpSessionId") String sessionId, @DestinationVariable("gameId") String gameId, @Payload MessageCastTargetedSpell message) {
		IGame g = games.get(gameId);
		if(g == null)
			return;
		
		IPlayer p = g.getPlayer(sessionId);
		if(p == null)
			return;
		
		g.receiveCastSpell(p.getName(), message.isOwn(), message.getCardId(), message.getTargetId());
	}

	@MessageMapping("/game/{gameId}/heroUntargetedSpecial")
	public void heroUntargetedSpecial(@Header("simpSessionId") String sessionId, @DestinationVariable("gameId") String gameId) {
		IGame g = games.get(gameId);
		if(g == null)
			return;
		
		IPlayer p = g.getPlayer(sessionId);
		if(p == null)
			return;
		
		g.receiveHeroSpecial(p.getName());
	}

	@MessageMapping("/game/{gameId}/heroTargetedSpecial")
	public void heroTargetedSpecial(@Header("simpSessionId") String sessionId, @DestinationVariable("gameId") String gameId, @Payload MessageHeroTargetedSpecial message) {
		IGame g = games.get(gameId);
		if(g == null)
			return;
		
		IPlayer p = g.getPlayer(sessionId);
		if(p == null)
			return;
		
		g.receiveHeroSpecial(p.getName(), message.isOwn(), message.getTargetId());
	}

	@MessageMapping("/game/{gameId}/endTurn")
	public void endTurn(@Header("simpSessionId") String sessionId, @DestinationVariable("gameId") String gameId) {
		IGame g = games.get(gameId);
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

	public void createGame(String id, User user1, User user2) throws Exception {
		Game g = new Game(id, template, user1, user2, cardMinionRepository, cardSpellRepository);
		games.put(id, g);
		
		System.out.println("create game with " + user1.getName() + " and " + user2.getName());

		String sendUser1 = new ObjectMapper().writeValueAsString(new Object() {
			@JsonProperty private String playerName = user1.getName();
			@JsonProperty private String playerHero = user1.getHeroType();
			@JsonProperty private String opponentName = user2.getName();
			@JsonProperty private String opponentHero = user2.getHeroType();
			@JsonProperty private String playing = g.getPlaying().getName(); // On considère que le premier a avoir cliqué commence
			@JsonProperty private String gameId = id;
		});
		String sendUser2 = new ObjectMapper().writeValueAsString(new Object() {
			@JsonProperty private String playerName = user2.getName();
			@JsonProperty private String playerHero = user2.getHeroType();
			@JsonProperty private String opponentName = user1.getName();
			@JsonProperty private String opponentHero = user1.getHeroType();
			@JsonProperty private String playing = g.getPlaying().getName();
			@JsonProperty private String gameId = id;
		});

		// User1 est celui qui a envoyer le acceptMatch
		template.convertAndSend("/topic/lobby/" + user1.getSessionId() + "/startGame", sendUser1);
		template.convertAndSend("/topic/lobby/" + user2.getSessionId() + "/startGame", sendUser2);
	}
}