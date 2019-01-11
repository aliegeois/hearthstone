package fr.ministone.controllertest;


import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.beans.factory.annotation.Autowired;


@Controller
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    //private BeerRepository repository;
    private SimpMessagingTemplate template;

    @Autowired
    public UserController(SimpMessagingTemplate template) {
        this.template = template;
    }

    /*
    @GetMapping("/good-beers")
    public Collection<Beer> goodBeers() {

        return repository.findAll().stream()

                .filter(this::isGreat)

                .collect(Collectors.toList());

    }*/

    /*
    @MessageMapping("/lobby/join")
    public void joinLobby(@Header("simpSessionId") String sessionId) {
        System.out.println("SessionId : " + sessionId);
        template.convertAndSend("/topic/lobby/", "lol");
    }*/


    @MessageMapping("/send/message")
    public void onReceivedMessage(String message) {
        System.out.println("Message reçu du client : " + message);
        template.convertAndSend("/topic/chat", message);
    }

    /*
    private boolean isGreat(Beer beer) {

        return !beer.getName().equals("Budweiser") &&

                !beer.getName().equals("Coors Light") &&

                !beer.getName().equals("PBR");

    }*/

}