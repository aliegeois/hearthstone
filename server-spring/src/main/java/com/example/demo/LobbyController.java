package com.example.demo;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.example.demo.message.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@Controller
public class LobbyController {
    private SimpMessagingTemplate template;
    private Map<String, String> players = new HashMap<>();
    //private List<String> players = new ArrayList<>();

    @Autowired
    public LobbyController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/lobby/join")
    public void joinLobby(@Payload MessageJoinLobby message, @Headers StompHeaderAccessor headers) throws Exception {
        // On envoie les infos sur une url spécifique pour chaque joueur (avec le nom du joueur dedans), chaque joueur se subscribe donc à l'url avec son nom
        
        template.convertAndSend("/topic/lobby/" + message.getPlayerName() + "/players", players);
        for(Map.Entry<String,String> pair : players.entrySet()) {
            template.convertAndSend("/topic/lobby/" + pair.getValue() + "/newPlayer", message.getPlayerName());
        }
        
        //players.add(message.getPlayerName());
        
        players.put(headers.getSessionId(), message.getPlayerName());
    }

    @EventListener
    public void onConnectEvent(SessionConnectEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        System.out.println("onConnect: " + headers.getSessionId());
    }

    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent event) {
        System.out.println("Client with username " + event + " disconnected");
    }
}