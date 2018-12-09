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
import java.util.Random;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class LobbyController {
	private class TemporaryGame {
		private User user1, user2;
		public TemporaryGame(User user1, User user2) {
			this.user1 = user1;
			this.user2 = user2;
		}
	}

	private SimpMessagingTemplate template;
	private GameController gameController;

	// <sessionId, utilisateur>
	private Map<String, User> users = new HashMap<>();
	private Map<String, User> waiting = new HashMap<>();
	private Map<UUID, TemporaryGame> temporaryGames = new HashMap<>();
	
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
					@JsonProperty private String name = pair.getValue().getName();
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
	@MessageMapping("/lobby/searchGame")
	public void createGame(@RequestHeader("sessionId") String sessionId) throws Exception {
		User user1 = users.get(sessionId);
		if(user1 == null) {
			// Erreur: l'un des deux utilisateur n'a pas été trouvé
			System.out.println("utilisateur(s) inconnus");
		} else if(waiting.containsKey(user1.getSessionId())) {
			// L'utilisateur est déjà en train de chercher une partie
			System.out.println("asking déjà en demande");
		} else {
			if(waiting.size() > 0) {
				List<User> tmpUsers = new ArrayList<>(waiting.values());
				User user2 = tmpUsers.get(new Random().nextInt(tmpUsers.size()));

				users.remove(sessionId);
				waiting.remove(user2.getSessionId());

				TemporaryGame tg = new TemporaryGame(user1, user2);
				UUID uuid = UUID.randomUUID();
				temporaryGames.put(uuid, tg);
				user1.setTemporaryGameId(uuid);
				user2.setTemporaryGameId(uuid);

				String sendUser1 = new ObjectMapper().writeValueAsString(new Object() {
					@JsonProperty private String opponent = user2.getName();
					@JsonProperty private String id = uuid.toString();
				});
				String sendUser2 = new ObjectMapper().writeValueAsString(new Object() {
					@JsonProperty private String opponent = user1.getName();
					@JsonProperty private String id = uuid.toString();
				});
				template.convertAndSend("/topic/lobby/" + user1.getName() + "/matchFound", sendUser1);
				template.convertAndSend("/topic/lobby/" + user2.getName() + "/matchFound", sendUser2);
			} else {
				users.remove(sessionId);
				waiting.put(sessionId, user1);
			}
		}
	}

	@MessageMapping("/lobby/acceptMatch")
	public void confirmGame(@RequestHeader("sessionId") String sessionId) throws Exception {
		User user1 = users.get(sessionId);
		if(user1 == null) {
			// Erreur
		} else {
			UUID gameId = user1.getTemporaryGameId();
			TemporaryGame tg = temporaryGames.get(gameId);
			if(tg == null) {
				// Encore une erreur
			} else {
				temporaryGames.remove(gameId);
				gameController.createGame(gameId, tg.user1, tg.user2);
			}
		}
	}

	@MessageMapping("/lobby/rejectMatch")
	public void rejectGame(@RequestHeader("sessionId") String sessionId) throws Exception {
		User user1 = users.get(sessionId);
		if(user1 == null) {
			// Erreur
		} else {
			UUID uuid = user1.getTemporaryGameId();
			TemporaryGame tg = temporaryGames.get(uuid);
			if(tg == null) {
				// Encore une erreur
			} else {
				User user2 = tg.user1 == user1 ? tg.user2 : tg.user1;
				user1.setTemporaryGameId(null);
				user2.setTemporaryGameId(null);
				String sendReject = new ObjectMapper().writeValueAsString(new Object() {
					@JsonProperty private String id = uuid.toString();
				});
				template.convertAndSend("/topic/lobby/" + user2.getName() + "/rejectMatch", sendReject);
				template.convertAndSend("/topic/lobby/" + user2.getName() + "/rejectMatch", sendReject);
			}
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

	/*private User getUserFromName(String name) {
		for(Map.Entry<String, User> pair : users.entrySet())
			if(pair.getValue().getName().equals(name))
				return pair.getValue();
		return null;
	}*/
}