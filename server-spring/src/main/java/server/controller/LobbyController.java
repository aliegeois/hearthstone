package server.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.bind.annotation.RequestHeader;
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

import java.util.ArrayList;
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
	public void joinLobby(@Payload MessageJoinLobby message, @RequestHeader("sessionId") String sessionId) throws Exception {
		// On envoie les infos sur une url spécifique pour chaque joueur (avec le nom du joueur dedans), chaque joueur se subscribe donc à l'url avec son nom
		// htmlEscape: pour éviter que l'utilisateur ne prenne un nom comme "<script>while(true)alert('');</script>"
		String name = HtmlUtils.htmlEscape(message.getName());
		if(users.containsKey(sessionId)) {
			// Le nom est déjà pris
		} else {
			ArrayList<Object> usersBefore = new ArrayList<>();
			for(Map.Entry<String, User> pair : users.entrySet()) {
				usersBefore.add(new Object() {
					@JsonProperty("name")
					private String name = pair.getValue().getName();
				});
			}
			template.convertAndSend("/topic/lobby/" + name + "/usersBefore", new ObjectMapper().writeValueAsString(usersBefore));
			for(Map.Entry<String, User> pair : users.entrySet())
				template.convertAndSend("/topic/lobby/" + pair.getValue() + "/userJoined", name);
			
			users.put(sessionId, new User(name, sessionId));
		}
	}

	@MessageMapping("/lobby/leave")
	public void leaveLobby(@RequestHeader("sessionId") String sessionId) throws Exception {
		if(users.containsKey(sessionId)) {
			User old = users.remove(sessionId);
			for(Map.Entry<String, User> pair : users.entrySet())
				template.convertAndSend("/topic/lobby/" + pair.getValue().getName() + "/userLeaved", old.getName());
		} else {
			// Un utilisateur qui n'existe pas leave ???
		}
	}

	// Lorsqu'un utilisateur veut créer une partie, une demande d'affrontement est envoyée au joueur adverse, si celui-ci accepte, la partie est créée
	@MessageMapping("/lobby/challenge")
	public void createGame(@Payload MessageCreateGame message, @RequestHeader("sessionId") String sessionId) throws Exception {
		User asking = users.get(sessionId);
		System.out.println("incomming sessionId: " + sessionId);
		User asked = getUserFromName(message.getOpponent());
		System.out.println("incomming userName: " + message.getOpponent());
		for(Map.Entry<String, User> pair : users.entrySet())
			System.out.println(pair.getKey() + " - " + pair.getValue().getName());
		if(asking == null || asked == null) {
			// Erreur: l'un des deux utilisateur n'a pas été trouvé
			System.out.println("utilisateur(s) inconnus");
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
			template.convertAndSend("/topic/lobby/" + askingName + "/confirmChallenge", sendAsking);
			template.convertAndSend("/topic/lobby/" + askedName + "/challengedBy", sendAsked);
		}
	}

	@MessageMapping("/lobby/{userName}/acceptChallenge")
	public void confirmGame(@DestinationVariable("userName") String userName, @RequestHeader("sessionId") String sessionId) throws Exception {
		User asking = getTemporaryFromName(userName);
		User asked = users.get(sessionId);
		if(asking == null || asked == null) {
			// Erreur
		} else {
			temporaryGames.remove(asking.getSessionId());
			users.remove(asked.getSessionId());
			gameController.createGame(asking, asked);
		}
		
		//template.convertAndSend("/topic/game/" + userName + "/test", "{\"value\": \"" + message.getValue() + "\"}");
	}

	@MessageMapping("/lobby/{gameId}/rejectChallenge")
	public void rejectGame(@DestinationVariable("gameId") String gameId, @Payload MessageTest message, @Headers StompHeaderAccessor headers) throws Exception {
		String sessionId = headers.getSessionId();
		if(temporaryGames.containsKey(sessionId)) {
			User rejected = temporaryGames.remove(sessionId);
			String sendRejected = new ObjectMapper().writeValueAsString(new Object() {
				@JsonProperty("name")
				private String name = rejected.getName();
			});
			template.convertAndSend("/topic/game/" + gameId + "/test", sendRejected);
		} else {

		}
		
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

	private User getTemporaryFromName(String name) {
		for(Map.Entry<String, User> pair : temporaryGames.entrySet())
			if(pair.getValue().getName().equals(name))
				return pair.getValue();
		return null;
	}
}