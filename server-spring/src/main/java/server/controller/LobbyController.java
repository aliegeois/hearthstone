package server.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import game.Game;
import server.message.*;
import server.*;
import java.util.Map;
import java.util.HashMap;

@Controller
public class LobbyController {
	private SimpMessagingTemplate template;
	private GameController gameController;

	private Map<String, User> users = new HashMap<>();

	@Autowired
	public LobbyController(SimpMessagingTemplate template, GameController gameController) {
		this.template = template;
		this.gameController = gameController;
	}
	
	@MessageMapping("/lobby/join")
	public void joinLobby(@Payload MessageJoinLobby message, @Headers StompHeaderAccessor headers) throws Exception {
		// On envoie les infos sur une url spécifique pour chaque joueur (avec le nom du joueur dedans), chaque joueur se subscribe donc à l'url avec son nom
		// htmlEscape: pour éviter que l'utilisateur ne prenne un nom comme "<script>while(true)alert('');</script>"
		String name = HtmlUtils.htmlEscape(message.getName());
		template.convertAndSend("/topic/lobby/" + name + "/users", users);
		for(Map.Entry<String,User> pair : users.entrySet())
			template.convertAndSend("/topic/lobby/" + pair.getValue() + "/newUser", name);
		
		users.put(headers.getSessionId(), new User(name));
	}

	// Lorsqu'un utilisateur veut créer une partie, une demande d'affrontement est envoyée au joueur adverse, si celui-ci accepte, la partie est créée
	@MessageMapping("/lobby/createGame")
	public void createGame(@Payload MessageCreateGame message, @Headers StompHeaderAccessor headers) throws Exception {
		User asking = users.get(headers.getSessionId());
		User asked = users.get(message.getOpponent());
		if(asking == null || asked == null) {
			// Erreur: l'un des deux utilisateur n'a pas été trouvé
		} else {
			String askingName = asking.getName();
			String askedName = asked.getName();
			MessageGameCreated gc = new MessageGameCreated(askingName, askedName);
			template.convertAndSend("/topic/lobby/" + askingName + "/test", "{\"value\": \"" + message.getOpponent() + "\"}");
		}
	}

	@MessageMapping("/lobby/{gameId}/confirm")
	public void confirmGame(@DestinationVariable("gameId") String gameId, @Payload MessageTest message) throws Exception {
		template.convertAndSend("/topic/game/" + gameId + "/test", "{\"value\": \"" + message.getValue() + "\"}");
	}

	@MessageMapping("/lobby/{gameId}/reject")
	public void rejectGame(@DestinationVariable("gameId") String gameId, @Payload MessageTest message) throws Exception {
		template.convertAndSend("/topic/game/" + gameId + "/test", "{\"value\": \"" + message.getValue() + "\"}");
	}

	@EventListener
	public void onConnectEvent(SessionConnectEvent event) {
		StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
		System.out.println("onConnect: " + headers.getSessionId());
	}

	@EventListener
	public void onDisconnectEvent(SessionDisconnectEvent event) {
		StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
		System.out.println("onDisconnect: " + headers.getSessionId());
		users.remove(headers.getSessionId());
	}
}