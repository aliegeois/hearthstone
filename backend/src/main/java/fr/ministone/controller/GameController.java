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

	@MessageMapping("/game/{gameId}/confirmStart")
	public void confirmStart(@Header("simpSessionId") String sessionId, @DestinationVariable("gameId") String gameId) {
		System.out.println("réception sur /game/" + gameId + "/confirmStart");

		for(IGame g : games.values())
			System.out.println("game " + g.getId());

		IGame g = games.get(gameId);
		if(g == null) {
			System.out.println("Game non trouvée");
			return;
		}
		
		IPlayer p = g.getPlayer(sessionId);
		if(p == null) {
			System.out.println("Player non trouvé");
			return;
		}
		
		g.receiveConfirmStart(p.getName());
	}

	@MessageMapping("/game/{gameId}/summonMinion")
	public void summonMinion(@Header("simpSessionId") String sessionId, @DestinationVariable("gameId") String gameId, @Payload MessageSummonMinion message) {
		System.out.println("réception sur /game/" + gameId + "/summonMinion de " + message);
		IGame g = games.get(gameId);
		if(g == null) {
			System.out.println("Game not found");
			return;
		}
			
		
		IPlayer p = g.getPlayer(sessionId);
		if(p == null) {
			System.out.println("Player non trouvé");
			return;
		}
		
		g.receiveSummonMinion(p.getName(), message.getCardId());
	}

	@MessageMapping("/game/{gameId}/attack")
	public void attack(@Header("simpSessionId") String sessionId, @DestinationVariable("gameId") String gameId, @Payload MessageAttack message) {
		System.out.println("réception sur /game/" + gameId + "/attack de " + message);
		IGame g = games.get(gameId);
		if(g == null) {
			System.out.println("Game not found");
			return;
		}
		
		IPlayer p = g.getPlayer(sessionId);
		if(p == null) {
			System.out.println("Player non trouvé");
			return;
		}
		
		g.receiveAttack(p.getName(), message.isHero(), message.getCardId(), message.getTargetId());
	}

	@MessageMapping("/game/{gameId}/castUntargetedSpell")
	public void castUntargetedSpell(@Header("simpSessionId") String sessionId, @DestinationVariable("gameId") String gameId, @Payload MessageCastUntargetedSpell message) {
		System.out.println("réception sur /game/" + gameId + "/castUntargetedSpell de " + message);
		IGame g = games.get(gameId);
		if(g == null) {
			System.out.println("Game not found");
			return;
		}
		
		IPlayer p = g.getPlayer(sessionId);
		if(p == null) {
			System.out.println("Player non trouvé");
			return;
		}
		
		g.receiveCastSpell(p.getName(), message.getCardId());
	}

	@MessageMapping("/game/{gameId}/castTargetedSpell")
	public void castTargetedSpell(@Header("simpSessionId") String sessionId, @DestinationVariable("gameId") String gameId, @Payload MessageCastTargetedSpell message) {
		System.out.println("réception sur /game/" + gameId + "/castTargetedSpell de " + message);
		IGame g = games.get(gameId);
		if(g == null) {
			System.out.println("Game not found");
			return;
		}
		
		IPlayer p = g.getPlayer(sessionId);
		if(p == null) {
			System.out.println("Player non trouvé");
			return;
		}
		
		g.receiveCastSpell(p.getName(), message.isOwn(), message.isHero(), message.getCardId(), message.getTargetId());
	}

	@MessageMapping("/game/{gameId}/heroUntargetedSpecial")
	public void heroUntargetedSpecial(@Header("simpSessionId") String sessionId, @DestinationVariable("gameId") String gameId) {
		System.out.println("réception sur /game/" + gameId + "/heroUntargetedSpecial");
		IGame g = games.get(gameId);
		if(g == null) {
			System.out.println("Game not found");
			return;
		}
		
		IPlayer p = g.getPlayer(sessionId);
		if(p == null) {
			System.out.println("Player non trouvé");
			return;
		}
		
		g.receiveHeroSpecial(p.getName());
	}

	@MessageMapping("/game/{gameId}/heroTargetedSpecial")
	public void heroTargetedSpecial(@Header("simpSessionId") String sessionId, @DestinationVariable("gameId") String gameId, @Payload MessageHeroTargetedSpecial message) {
		System.out.println("réception sur /game/" + gameId + "/heroTargetedSpecial de " + message);
		IGame g = games.get(gameId);
		if(g == null) {
			System.out.println("Game not found");
			return;
		}
		
		IPlayer p = g.getPlayer(sessionId);
		if(p == null) {
			System.out.println("Player non trouvé");
			return;
		}
		
		g.receiveHeroSpecial(p.getName(), message.isOwn(), message.isHero(), message.getTargetId());
	}

	@MessageMapping("/game/{gameId}/endTurn")
	public void endTurn(@Header("simpSessionId") String sessionId, @DestinationVariable("gameId") String gameId) {
		System.out.println("réception sur /game/" + gameId + "/endTurn");
		IGame g = games.get(gameId);
		if(g == null) {
			System.out.println("Game not found");
			return;
		}
		
		IPlayer p = g.getPlayer(sessionId);
		if(p == null) {
			System.out.println("Player non trouvé");
			return;
		}
		
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

	public void createGame(String gId, User user1, User user2) throws Exception {
		Game g = new Game(gId, template, user1, user2, cardMinionRepository, cardSpellRepository);
		games.put(gId, g);

		System.out.println("Les deux joueurs ont accepté");
		String sendUser1 = new ObjectMapper().writeValueAsString(new Object() {
			@JsonProperty private String playerName = user1.getName();
			@JsonProperty private String playerHero = user1.getHeroType();
			@JsonProperty private String opponentName = user2.getName();
			@JsonProperty private String opponentHero = user2.getHeroType();
			@JsonProperty private String playing = g.getPlaying().getName(); // On considère que le premier a avoir cliqué commence
			@JsonProperty private String gameId = gId;
		});
		String sendUser2 = new ObjectMapper().writeValueAsString(new Object() {
			@JsonProperty private String playerName = user2.getName();
			@JsonProperty private String playerHero = user2.getHeroType();
			@JsonProperty private String opponentName = user1.getName();
			@JsonProperty private String opponentHero = user1.getHeroType();
			@JsonProperty private String playing = g.getPlaying().getName();
			@JsonProperty private String gameId = gId;
		});

		// User1 est celui qui a envoyer le acceptMatch
		template.convertAndSend("/topic/lobby/" + user1.getSessionId() + "/startGame", sendUser1);
		template.convertAndSend("/topic/lobby/" + user2.getSessionId() + "/startGame", sendUser2);

		//g.start();
		
		System.out.println("create game with " + user1.getName() + " and " + user2.getName());
	}
}