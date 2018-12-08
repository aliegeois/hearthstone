package server.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import server.message.*;
import game.*;
import java.util.Map;
import java.util.HashMap;
//import org.springframework.web.util.HtmlUtils;

@Controller
public class GameController {
	// Pour envoyer des messages sans utiliser "@SendTo"
	private SimpMessagingTemplate template;
	// Liste des parties en cours
	private Map<String, Game> games;

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

	// Lorsqu'un utilisateur veut créer une partie, une demande d'affrontement est envoyée au joueur adverse, si celui-ci accepte, la partie est créée
	@MessageMapping("/game/create")
	public void createGame(@DestinationVariable("gameId") String gameId, @Payload MessageCreateGame message) throws Exception {
		MessageGameCreated gc = new MessageGameCreated()
		template.convertAndSend("/topic/game/" + gameId + "/test", "{\"value\": \"" + message.getOpponent() + "\"}");
	}

	@MessageMapping("/game/{gameId}/confirm")
	public void confirmGame(@DestinationVariable("gameId") String gameId, @Payload MessageTest message) throws Exception {
		template.convertAndSend("/topic/game/" + gameId + "/test", "{\"value\": \"" + message.getValue() + "\"}");
	}

	@MessageMapping("/game/{gameId}/reject")
	public void rejectGame(@DestinationVariable("gameId") String gameId, @Payload MessageTest message) throws Exception {
		template.convertAndSend("/topic/game/" + gameId + "/test", "{\"value\": \"" + message.getValue() + "\"}");
	}

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

	public void createGame() {

	}
}