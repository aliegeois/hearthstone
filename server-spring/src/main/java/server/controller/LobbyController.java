package server.controller;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import server.message.*;
import server.*;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class LobbyController {
	private class TemporaryGame {
		private Map<String, User> users = new HashMap<>();
		private Map<String, Boolean> accept = new HashMap<>();
		public TemporaryGame(User user1, User user2) {
			this.users.put(user1.getName(), user1);
			this.users.put(user2.getName(), user2);
			this.accept.put(user1.getName(), false);
			this.accept.put(user2.getName(), false);
		}
	}

	private SimpMessagingTemplate template;
	private GameController gameController;

	// <userName, user>
	private Map<String, User> users = new HashMap<>();
	//private Map<String, User> waiting = new HashMap<>();
	User waiting = null;
	private Map<UUID, TemporaryGame> temporaryGames = new HashMap<>();
	
	@Autowired
	public LobbyController(SimpMessagingTemplate template, GameController gameController) {
		this.template = template;
		this.gameController = gameController;
	}
	
	@MessageMapping("/lobby/join")
	public void joinLobby(@Payload MessageJoinLobby message, @Header("simpSessionId") String sessionId) throws Exception {
		// On envoie les infos sur une url spécifique pour chaque joueur (avec le nom du joueur dedans), chaque joueur se subscribe donc à l'url avec son nom
		// htmlEscape: pour éviter que l'utilisateur ne prenne un nom comme "<script>while(true)alert('');</script>"
		String userName = HtmlUtils.htmlEscape(message.getName().trim());
		if(users.containsKey(userName)) {
			// Le nom est déjà pris
			String sendError = new ObjectMapper().writeValueAsString(new Object() {
				@JsonProperty private String message = "Name already taken";
			});
			template.convertAndSend("/topic/lobby/" + sessionId + "/error", sendError);
		} else {
			String sendName = new ObjectMapper().writeValueAsString(new Object() {
				@JsonProperty private String name = userName;
			});
			template.convertAndSend("/topic/lobby/" + sessionId + "/confirmName", sendName);

			ArrayList<Object> usersBefore = new ArrayList<>();
			for(Map.Entry<String, User> pair : users.entrySet()) {
				usersBefore.add(new Object() {
					@JsonProperty private String name = pair.getValue().getName();
				});
			}
			template.convertAndSend("/topic/lobby/" + sessionId + "/usersBefore", new ObjectMapper().writeValueAsString(usersBefore));

			for(Map.Entry<String, User> pair : users.entrySet())
				template.convertAndSend("/topic/lobby/" + pair.getValue().getSessionId() + "/userJoined", sendName);
			
			users.put(userName, new User(userName, sessionId));
		}
	}

	@MessageMapping("/lobby/leave")
	public void leaveLobby(@Header("simpSessionId") String sessionId) throws Exception {
		if(containsSessionId(sessionId)) {
			User old = users.remove(getBySessionId(sessionId).getName());
			String sendLeave = new ObjectMapper().writeValueAsString(new Object() {
				@JsonProperty private String name = old.getName();
			});
			for(Map.Entry<String, User> pair : users.entrySet())
				template.convertAndSend("/topic/lobby/" + pair.getValue().getSessionId() + "/userLeaved", sendLeave);
		} else {
			// Un utilisateur qui n'existe pas leave ???
		}
	}

	// Lorsqu'un utilisateur veut créer une partie, une demande d'affrontement est envoyée au joueur adverse, si celui-ci accepte, la partie est créée
	@MessageMapping("/lobby/searchGame")
	public void createGame(@Header("simpSessionId") String sessionId) throws Exception {
		if(containsSessionId(sessionId)) {
			User user1 = getBySessionId(sessionId);
			// Si le joueur est le premier à ce mettre en file d'attente
			if(waiting == null) {
				waiting = users.remove(user1.getName());
				System.out.println("Premier joueur en attente");
			// Si le joueur est déjà en attente
			} else if(waiting.getName().equals(user1.getName())) {
				// Grosse erreur de cohérence
				System.out.println("Même joueur deux fois dans la file d'attente");
			// S'il y a déjà un joueur en attente
			} else {
				System.out.println("Deuxième joueur en attente");
				User user2 = waiting;
				users.remove(user1.getName());
				waiting = null;

				TemporaryGame tg = new TemporaryGame(user1, user2);
				UUID gameId = UUID.randomUUID();
				temporaryGames.put(gameId, tg);
				user1.setTemporaryGameId(gameId);
				user2.setTemporaryGameId(gameId);

				String sendUser1 = new ObjectMapper().writeValueAsString(new Object() {
					@JsonProperty private String opponent = user2.getName();
					@JsonProperty private String id = gameId.toString();
				});
				String sendUser2 = new ObjectMapper().writeValueAsString(new Object() {
					@JsonProperty private String opponent = user1.getName();
					@JsonProperty private String id = gameId.toString();
				});
				template.convertAndSend("/topic/lobby/" + user1.getSessionId() + "/matchFound", sendUser1);
				template.convertAndSend("/topic/lobby/" + user2.getSessionId() + "/matchFound", sendUser2);
			}
		} else {
			// Erreur
		}
	}

	@MessageMapping("/lobby/acceptMatch")
	public void confirmGame(@Header("simpSessionId") String sessionId) throws Exception {
		if(containsSessionId(sessionId)) {
			User user = getBySessionId(sessionId);
			UUID gameId = user.getTemporaryGameId();
			if(temporaryGames.containsKey(gameId)) {
				TemporaryGame tg = temporaryGames.get(gameId);
				temporaryGames.remove(gameId);
				//gameController.createGame(gameId, tg.user1, tg.user2);
			} else {
				// Encore une erreur
			}
		} else {
			// Erreur
		}
	}

	@MessageMapping("/lobby/declineMatch")
	public void declineGame(@Header("simpSessionId") String sessionId) throws Exception {
		if(containsSessionId(sessionId)) {
			User user1 = getBySessionId(sessionId);
			UUID gameId = user1.getTemporaryGameId();
			if(temporaryGames.containsKey(gameId)) {
				TemporaryGame tg = temporaryGames.get(gameId);
				//User user2 = tg.user1.getName().equals(user1.getName()) ? tg.user2 : tg.user1;
				User user2 = null;
				user1.setTemporaryGameId(null);
				user2.setTemporaryGameId(null);
				temporaryGames.remove(gameId);
				String sendDecline = new ObjectMapper().writeValueAsString(new Object() {
					@JsonProperty private String id = gameId.toString();
				});
				template.convertAndSend("/topic/lobby/" + user2.getSessionId() + "/declineMatch", sendDecline);
				template.convertAndSend("/topic/lobby/" + user2.getSessionId() + "/declineMatch", sendDecline);
			} else {
				// Encore une erreur
			}
		} else {
			// Erreur
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
		try {
			leaveLobby(headers.getSessionId());
		} catch(Exception e) {}
	}

	private boolean containsSessionId(String sessionId) {
		return getBySessionId(sessionId) != null;
	}

	private User getBySessionId(String sessionId) {
		for(Map.Entry<String, User> pair : users.entrySet())
			if(pair.getValue().getSessionId().equals(sessionId))
				return pair.getValue();
		return null;
	}

	/*private User getUserBySessionId(String sessionId) {
		for(Map.Entry<String, User> pair : users.entrySet())
			if(pair.getValue().getSessionId().equals(sessionId))
				return pair.getValue();
		return null;
	}*/
}