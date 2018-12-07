package com.example.demo;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.example.demo.message.*;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

@Controller
public class LobbyController {
    private SimpMessagingTemplate template;
    private List<String> players = new ArrayList<>();

    @Autowired
    public LobbyController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/lobby/join")
    public void joinLobby(MessageJoinLobby message) throws Exception {
        // On envoie les infos sur une url spécifique pour chaque joueur (avec le nom du joueur dedans), chaque joueur se subscribe donc à l'url avec son nom

        template.convertAndSend("/topic/lobby/" + message.getPlayerName() + "/players", players);
        for(String name : players) {
            template.convertAndSend("/topic/lobby/" + name + "/newPlayer", message.getPlayerName());
        }
        
        players.add(message.getPlayerName());
    }
}