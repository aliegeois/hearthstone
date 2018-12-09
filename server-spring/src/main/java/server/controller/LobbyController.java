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

import game.*;
import server.message.*;
import server.*;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

@Controller
public class LobbyController {
	private SimpMessagingTemplate template;
	private GameController gameController;

	// <sessionId, utilisateur>
	private Map<String, User> users = new HashMap<>();
	private Map<String, User> temporaryGames = new HashMap<>();
	
	@Autowired
	public LobbyController(SimpMessagingTemplate template, GameController gameController) {
		this.template = template;
		this.gameController = gameController;
		// Test
		this.users.put("___", new User("Billy", "___"));
	}
	
	@MessageMapping("/lobby/join")
	public void joinLobby(@Payload MessageJoinLobby message, @Headers StompHeaderAccessor headers) throws Exception {
		// On envoie les infos sur une url spécifique pour chaque joueur (avec le nom du joueur dedans), chaque joueur se subscribe donc à l'url avec son nom
		// htmlEscape: pour éviter que l'utilisateur ne prenne un nom comme "<script>while(true)alert('');</script>"
		String name = HtmlUtils.htmlEscape(message.getName());
		String sessionId = headers.getSessionId();
		if(users.containsKey(sessionId)) {
			// Le nom est déjà pris
		} else {
			template.convertAndSend("/topic/lobby/" + name + "/users", users);
			for(Map.Entry<String, User> pair : users.entrySet())
				template.convertAndSend("/topic/lobby/" + pair.getValue() + "/userJoined", name);
			
			users.put(sessionId, new User(name, sessionId));
		}
	}

	@MessageMapping("/lobby/leave")
	public void leaveLobby(@Payload MessageJoinLobby message, @Headers StompHeaderAccessor headers) throws Exception {
		// On envoie les infos sur une url spécifique pour chaque joueur (avec le nom du joueur dedans), chaque joueur se subscribe donc à l'url avec son nom
		// htmlEscape: pour éviter que l'utilisateur ne prenne un nom comme "<script>while(true)alert('');</script>"
		String name = HtmlUtils.htmlEscape(message.getName());
		String sessionId = headers.getSessionId();
		if(users.containsKey(sessionId)) {
			// Un utilisateur qui n'existe pas leave ???
		} else {
			users.remove(sessionId);
			for(Map.Entry<String, User> pair : users.entrySet())
				template.convertAndSend("/topic/lobby/" + pair.getValue().getName() + "/userLeaved", name);
		}
	}

	// Lorsqu'un utilisateur veut créer une partie, une demande d'affrontement est envoyée au joueur adverse, si celui-ci accepte, la partie est créée
	@MessageMapping("/lobby/createGame")
	public void createGame(@Payload MessageCreateGame message, @Headers StompHeaderAccessor headers) throws Exception {
		User asking = users.get(headers.getSessionId());
		System.out.println("incomming sessionId: " + headers.getSessionId());
		User asked = getUserFromName(message.getOpponent());
		System.out.println("incomming userName: " + message.getOpponent());
		for(Map.Entry<String, User> pair : users.entrySet())
			System.out.println(pair.getKey() + " - " + pair.getValue().getName());
		if(asking == null || asked == null) {
			// Erreur: l'un des deux utilisateur n'a pas été trouvé
			System.out.println("Utilisateurs inconnus");
		} else if(temporaryGames.containsKey(asking.getSessionId())) {
			// L'utilisateur est déjà en train de créer une partie, or il ne peut en avoir qu'une seule à la fois
			System.out.println("asking déjà en demande");
		} else if(temporaryGames.containsKey(asked.getSessionId())) {
			// l'utilisateur défié est déjà en train de créer une partie, on ne peut pas le déranger
			System.out.println("asked déjà en demande");
		} else {
			String askingName = asking.getName();
			String askedName = asked.getName();
			temporaryGames.put(asking.getSessionId(), asking);
			String sendAsking = new ObjectMapper().writeValueAsString(new Object() {
				@JsonProperty("asked")
				private String asked = askedName;
			});
			String sendAsked = new ObjectMapper().writeValueAsString(new Object() {
				@JsonProperty("asking")
				private String asking = askingName;
			});
			System.out.println("ok - " + sendAsking + " - " + sendAsked);
			template.convertAndSend("/topic/lobby/" + askingName + "/confirmCreateGame", sendAsking);
			template.convertAndSend("/topic/lobby/" + askedName + "/askCreateGame", sendAsked);
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

	private User getUserFromName(String name) {
		for(Map.Entry<String, User> pair : users.entrySet())
			if(pair.getValue().getName().equals(name))
				return pair.getValue();
		return null;
	}
}