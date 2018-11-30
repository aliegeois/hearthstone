package com.example.demo;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import com.example.demo.message.*;
//import org.springframework.web.util.HtmlUtils;

@Controller
public class ActionController {

    /*@MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        //Thread.sleep(1000); // simulated delay
        //return new Greeting("Bonjour, " + HtmlUtils.htmlEscape(message.getName()) + " !");
        return new Greeting("Bonjour, " + message.getName() + " !");
    }*/

    @MessageMapping("/attack")
    @SendTo("/topic/attack")
    public MessageAttack attack(MessageAttack message) throws Exception {
        // Le message reçu et le message envoyé sont du même type
        // modifications côté serveur
        
        return new MessageAttack(/* des trucs */);
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
}