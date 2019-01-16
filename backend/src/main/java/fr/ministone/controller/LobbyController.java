package fr.ministone.controller;

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
import org.springframework.web.bind.annotation.CrossOrigin;

import fr.ministone.message.*;
import fr.ministone.User;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
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
	private Map<String, User> users = new HashMap<>(); // AMELIORER
	//private Map<String, User> waiting = new HashMap<>();
	Map<String, User> waiting = null; //Une liste pour les user novice, une pour les users regular, et une pour les users expert
	private Map<Long, TemporaryGame> temporaryGames = new HashMap<>();
	
	@Autowired
	public LobbyController(SimpMessagingTemplate template, GameController gameController) {
		this.template = template;
		this.gameController = gameController;

		this.waiting = new HashMap<String, User>();
	}
	
	@MessageMapping("/lobby/join")
	public void joinLobby(@Header("simpSessionId") String sessionId, @Payload MessageJoinLobby message) throws Exception {
		// On envoie les infos sur une url spécifique pour chaque joueur (avec le nom du joueur dedans), chaque joueur se subscribe donc à l'url avec son nom
		// htmlEscape: pour éviter que l'utilisateur ne prenne un nom comme "<script>while(true)alert('');</script>"
		String userName = HtmlUtils.htmlEscape(message.getName().trim());
		String userLevel = message.getLevel();
		String userHeroType = message.getHeroType();

		System.out.println("Level recu : " + userLevel);
		System.out.println("HeroType recu + " + userHeroType);

		if(users.containsKey(userName)) {
			// Le nom est déjà pris
			String sendError = new ObjectMapper().writeValueAsString(new Object() {
				@JsonProperty private String message = "Name already taken";
			});
			System.out.println("Le nom existe déjà");
			template.convertAndSend("/topic/lobby/" + sessionId + "/error", sendError);
		} else {
			String sendPlayer = new ObjectMapper().writeValueAsString(new Object() {
				@JsonProperty private String name = userName;
				@JsonProperty private String level = userLevel;
				@JsonProperty private String heroType = userHeroType;
			});
			System.out.println("Confirmation du nom : envoi sur /topic/lobby/" + sessionId + "/confirmName");

			template.convertAndSend("/topic/lobby/" + sessionId + "/confirmName", sendPlayer);

			ArrayList<Object> usersBefore = new ArrayList<>();
			for(Map.Entry<String, User> pair : users.entrySet()) {
				usersBefore.add(new Object() {
					@JsonProperty private String name = pair.getValue().getName();
					@JsonProperty private String level = pair.getValue().getLevel();
					@JsonProperty private String heroType = pair.getValue().getHeroType();
				});
			}
			template.convertAndSend("/topic/lobby/" + sessionId + "/usersBefore", new ObjectMapper().writeValueAsString(usersBefore));

			for(Map.Entry<String, User> pair : users.entrySet())
				template.convertAndSend("/topic/lobby/" + pair.getValue().getSessionId() + "/userJoined", sendPlayer);
			
			// users.put(userName, new User(userName, sessionId, userLevel));
			users.put(userName, new User(userName, sessionId, userLevel, userHeroType));
		}
	}

	@MessageMapping("/lobby/leave")
	public void leaveLobby(@Header("simpSessionId") String sessionId) throws Exception {
		System.out.println("reçu sur /lobby/leave");
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
		System.out.println("reçu sur /lobby/searchGame");
		if(!containsSessionId(sessionId)) {
			return;
		}

		User user1 = getBySessionId(sessionId); //
		String user1Level = user1.getLevel(); //

		System.out.println("Level du joueur qui vient de rechercher une game : " + user1Level);
		// Si le joueur est le premier à se mettre en file d'attente
		if(waiting.get(user1Level) == null) {
			waiting.put(user1Level, users.get(user1.getName()));
			//waiting = users.get(user1.getName());
			System.out.println("Premier joueur en attente");

		// Si le joueur est déjà en attente
		} else if(waiting.get(user1Level).getName().equals(user1.getName())) {
			// Grosse erreur de cohérence
			System.out.println("Même joueur deux fois dans la file d'attente");
		// S'il y a déjà un joueur en attente
		} else {
			System.out.println("Deuxième joueur en attente");
			User user2 = waiting.get(user1Level);
			//users.remove(user1.getName());
			waiting.put(user1Level, null);
			TemporaryGame tg = new TemporaryGame(user1, user2);
			Long gameId = Math.abs(UUID.randomUUID().getLeastSignificantBits());
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
		System.out.println("reçu sur /lobby/acceptMatch");
		if(!isInTemporaryGame(sessionId)) {
			return;
		}
		
		User user1 = getFromTemporaryGame(sessionId);
		Long gId = user1.getTemporaryGameId();
		TemporaryGame tg = temporaryGames.get(gId);
		User user2 = user1.getOpponent();

		if(tg.hasAccepted(user2)) { // L'adversaire a déjà accepté
			System.out.println("Les deux joueurs ont accepté");

			gameController.createGame(gId, user1, user2);
			temporaryGames.remove(gId);
			users.remove(sessionId);
			users.remove(user2.getSessionId());
		} else { // L'adversaire n'a pas encore accepté
			tg.accept(user1);
		}
	}

	@MessageMapping("/lobby/declineMatch")
	public void declineGame(@Header("simpSessionId") String sessionId) throws Exception {
		System.out.println("reçu sur /lobby/declineMatch");
		if(!isInTemporaryGame(sessionId)) {
			return;
		}

		User user1 = getFromTemporaryGame(sessionId);
		Long gId = user1.getTemporaryGameId();
		//TemporaryGame tg = temporaryGames.get(gameId);
		User user2 = user1.getOpponent();

		user1.setTemporaryGameId(null);
		user2.setTemporaryGameId(null);
		temporaryGames.remove(gId);

		String sendDeclineUser1 = new ObjectMapper().writeValueAsString(new Object() {
			@JsonProperty private Long gameId = gId;
			@JsonProperty private String opponent = user2.getName();
		});
		String sendDeclineUser2 = new ObjectMapper().writeValueAsString(new Object() {
			@JsonProperty private Long gameIdd = gId;
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
		for(Map.Entry<Long, TemporaryGame> pair : temporaryGames.entrySet())
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