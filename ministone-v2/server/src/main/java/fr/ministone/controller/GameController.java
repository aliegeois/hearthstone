package fr.ministone.controller;

import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import fr.ministone.User;
import fr.ministone.Game;
import fr.ministone.message.*;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;
//import org.springframework.web.util.HtmlUtils;

@Controller
public class GameController {
	// Pour envoyer des messages sans utiliser "@SendTo"
	private SimpMessagingTemplate template;
	// Liste des parties en cours
	private Map<UUID, Game> games;

	@Autowired
	public GameController(SimpMessagingTemplate template) {
		this.template = template;
		this.games = new HashMap<>();
	}

	/*@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public Greeting greeting(HelloMessage message) throws Exception {
		//Thread.sleep(1000); // simulated delay
		//return new Greeting("Bonjour, " + HtmlUtils.htmlEscape(message.getName()) + " !");
		return new Greeting("Bonjour, " + message.getName() + " !");
	}*/

	// SITE DE LA VIE: https://www.programcreek.com/java-api-examples/?api=org.springframework.messaging.handler.annotation.MessageMapping

	//@DestinationVariable("gameId") String gameId met le contenu de la variable gameId dans une string gameId que l'on peut ensuite utiliser
	//Le MessageTest message est le message qui transite.
	/*@MessageMapping("/game/{gameId}")
	@SendTo("/topic/")
	public MessageTest test(@DestinationVariable("gameId") String gameId, @Payload MessageTest message) throws Exception {
		// Le message reçu et le message envoyé sont du même type
		// modifications côté serveur
		
		return new MessageTest(gameId);
	}*/

	/*@MessageMapping("/lobby")
	@SendTo("/topic/lobby")
	public MessageJoinLobby joinLobby(MessageJoinLobby message) throws Exception {
		// Stocker le joueur qui viens d'arriver

		return message;
	}*/



	@MessageMapping("/game/{gameId}/test")
	public void test2(@DestinationVariable("gameId") String gameId, @Payload MessageTest message) throws Exception {
		template.convertAndSend("/topic/game/" + gameId + "/test", "{\"value\": \"" + message.getValue() + "\"}");
	}

	@MessageMapping("/game/{gameId}/attack")
	@SendTo("/topic/attack")
	public MessageAttack attack(@DestinationVariable("gameId") String gameId, @Payload MessageAttack message) throws Exception {
		return new MessageAttack();
	}

	@MessageMapping("/summonMinion")
	@SendTo("/topic/summonMinion")
	public MessageSummonMinion summonMinion(MessageSummonMinion message) throws Exception {
		// Le message reçu et le message envoyé sont du même type
		// modifications côté serveur

		return new MessageSummonMinion(/* des trucs */);
	}

	@MessageMapping("/castTargetedSpell")
	@SendTo("/topic/castTargetedSpell")
	public MessageCastTargetedSpell castTargetedSpell(MessageCastTargetedSpell message) throws Exception {
		// Le message reçu et le message envoyé sont du même type
		// modifications côté serveur

		return new MessageCastTargetedSpell(/* des trucs */);
	}

	@MessageMapping("/castUntargetedSpell")
	@SendTo("/topic/castUntargetedSpell")
	public MessageCastUntargetedSpell castUntargetedSpell(MessageCastUntargetedSpell message) throws Exception {
		// Le message reçu et le message envoyé sont du même type
		// modifications côté serveur

		return new MessageCastUntargetedSpell(/* des trucs */);
	}

	@MessageMapping("/targetedSpecial")
	@SendTo("/topic/targetedSpecial")
	public MessageTargetedSpecial targetedSpecial(MessageTargetedSpecial message) throws Exception {
		// Le message reçu et le message envoyé sont du même type
		// modifications côté serveur

		return new MessageTargetedSpecial(/* des trucs */);
	}

	@MessageMapping("/untargetedSpecial")
	@SendTo("/topic/untargetedSpecial")
	public MessageUntargetedSpecial untargetedSpecial(MessageUntargetedSpecial message) throws Exception {
		// Le message reçu et le message envoyé sont du même type
		// modifications côté serveur

		return new MessageUntargetedSpecial(/* des trucs */);
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

	public void createGame(UUID gameId, User player1, User player2) {
		Game g = new Game(gameId, player1, player2);
		games.put(gameId, g);
		
		System.out.println("create game with " + player1.getName() + " and " + player2.getName());
	}
}