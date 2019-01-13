package fr.ministone.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.ministone.game.card.CardMinion;
import fr.ministone.game.card.CardSpell;
import fr.ministone.repository.CardMinionRepository;
import fr.ministone.repository.CardSpellRepository;

@Controller
@RequestMapping(path="/cards")
public class CardController {
    private CardMinionRepository cardMinionRepository;
    private CardSpellRepository cardSpellRepository;

    @GetMapping(path="/getMinion") // Map ONLY GET Requests
	public @ResponseBody CardMinion getCardMinionByName (@RequestParam String name) {
		return cardMinionRepository.findByName(name);
    }
    
    @GetMapping(path="/getSpell") // Map ONLY GET Requests
	public @ResponseBody CardSpell getCardSpellByName (@RequestParam String name) {
		return cardSpellRepository.findByName(name);
	}

    @GetMapping(path="/allMinions")
	public @ResponseBody Iterable<CardMinion> getAllMinions() {
		// This returns a JSON or XML with the users
		return cardMinionRepository.findAll();
    }

    @GetMapping(path="/allSpells")
	public @ResponseBody Iterable<CardSpell> getAllSpells() {
		// This returns a JSON or XML with the users
		return cardSpellRepository.findAll();
	}
}