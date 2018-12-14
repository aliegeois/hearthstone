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
			user1.setOpponent(user2);
			this.users.put(user2.getName(), user2);
			user2.setOpponent(user1);
			this.accept.put(user1.getName(), false);
			this.accept.put(user2.getName(), false);
		}

		public boolean hasUser(String sessionId) {
			return getUser(sessionId) != null;
		}

		public User getUser(String sessionId) {
			for(Map.Entry<String, User> pair : users.entrySet())
				if(pair.getValue().getSessionId().equals(sessionId))
					return pair.getValue();
			return null;
		}

		public Boolean hasAccepted(User u) {
			return accept.get(u.getName());
		}

		public void accept(User u) {
			accept.put(u.getName(), true);
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
		this.users.put("_", new User("Billy", "_"));
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
		if(!containsSessionId(sessionId)) {
			return;
		}

		User old = users.remove(getBySessionId(sessionId).getName());
		String sendLeave = new ObjectMapper().writeValueAsString(new Object() {
			@JsonProperty private String name = old.getName();
		});
		for(Map.Entry<String, User> pair : users.entrySet()) {
			template.convertAndSend("/topic/lobby/" + pair.getValue().getSessionId() + "/userLeaved", sendLeave);
		}
	}

	// Lorsqu'un utilisateur veut créer une partie, une demande d'affrontement est envoyée au joueur adverse, si celui-ci accepte, la partie est créée
	@MessageMapping("/lobby/searchGame")
	public void createGame(@Header("simpSessionId") String sessionId) throws Exception {
		if(!containsSessionId(sessionId)) {
			return;
		}

		User user1 = getBySessionId(sessionId);
		// Si le joueur est le premier à se mettre en file d'attente
		if(waiting == null) {
			waiting = users.get(user1.getName());
			System.out.println("Premier joueur en attente");
		// Si le joueur est déjà en attente
		} else if(waiting.getName().equals(user1.getName())) {
			// Grosse erreur de cohérence
			System.out.println("Même joueur deux fois dans la file d'attente");
		// S'il y a déjà un joueur en attente
		} else {
			System.out.println("Deuxième joueur en attente");
			User user2 = waiting;
			//users.remove(user1.getName());
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
	}

	@MessageMapping("/lobby/acceptMatch")
	public void confirmGame(@Header("simpSessionId") String sessionId) throws Exception {
		if(!isInTemporaryGame(sessionId)) {
			return;
		}
		
		User user1 = getFromTemporaryGame(sessionId);
		UUID gameId = user1.getTemporaryGameId();
		TemporaryGame tg = temporaryGames.get(gameId);
		User user2 = user1.getOpponent();

		if(tg.hasAccepted(user2)) { // L'adversaire a déjà accepté
			System.out.println("Les deux joueurs ont accepté");
			String sendUser1 = new ObjectMapper().writeValueAsString(new Object() {
				@JsonProperty private String opponent = user2.getName();
			});
			String sendUser2 = new ObjectMapper().writeValueAsString(new Object() {
				@JsonProperty private String opponent = user1.getName();
			});
			template.convertAndSend("/topic/lobby/" + user1.getSessionId() + "/startGame", sendUser1);
			template.convertAndSend("/topic/lobby/" + user2.getSessionId() + "/startGame", sendUser2);
			gameController.createGame(gameId, user1, user2);
			temporaryGames.remove(gameId);
			users.remove(sessionId);
			users.remove(user2.getSessionId());
		} else { // L'adversaire n'a pas encore accepté
			tg.accept(user1);
		}
	}

	@MessageMapping("/lobby/declineMatch")
	public void declineGame(@Header("simpSessionId") String sessionId) throws Exception {
		if(!isInTemporaryGame(sessionId)) {
			return;
		}

		User user1 = getFromTemporaryGame(sessionId);
		UUID gameId = user1.getTemporaryGameId();
		TemporaryGame tg = temporaryGames.get(gameId);
		User user2 = user1.getOpponent();

		user1.setTemporaryGameId(null);
		user2.setTemporaryGameId(null);
		temporaryGames.remove(gameId);

		String sendDeclineUser1 = new ObjectMapper().writeValueAsString(new Object() {
			@JsonProperty private String id = gameId.toString();
			@JsonProperty private String opponent = user2.getName();
		});
		String sendDeclineUser2 = new ObjectMapper().writeValueAsString(new Object() {
			@JsonProperty private String id = gameId.toString();
			@JsonProperty private String opponent = user1.getName();
		});
		template.convertAndSend("/topic/lobby/" + user1.getSessionId() + "/matchDeclined", sendDeclineUser1);
		template.convertAndSend("/topic/lobby/" + user2.getSessionId() + "/matchDeclined", sendDeclineUser2);
	}

	@EventListener
	public void onConnectEvent(SessionConnectEvent event) {
		StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
		System.out.println("onConnect (lobby): " + headers.getSessionId());
	}

	@EventListener
	public void onDisconnectEvent(SessionDisconnectEvent event) {
		StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
		System.out.println("onDisconnect (lobby): " + headers.getSessionId());
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

	public boolean isInTemporaryGame(String sessionId) {
		return getFromTemporaryGame(sessionId) != null;
	}

	public User getFromTemporaryGame(String sessionId) {
		for(Map.Entry<UUID, TemporaryGame> pair : temporaryGames.entrySet())
			if(pair.getValue().hasUser(sessionId))
				return pair.getValue().getUser(sessionId);
		return null;
	}

	/*private User getUserBySessionId(String sessionId) {
		for(Map.Entry<String, User> pair : users.entrySet())
			if(pair.getValue().getSessionId().equals(sessionId))
				return pair.getValue();
		return null;
	}*/
}