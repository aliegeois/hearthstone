package fr.ministone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.ministone.game.card.*;
import fr.ministone.repository.*;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path="/cards")
public class CardController {
	@Autowired
	private CardMinionRepository cardMinionRepository;
	@Autowired
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

	@GetMapping(path="/deckMinion")
	public @ResponseBody Iterable<CardMinion> getMinionByDeck(@RequestParam String deck) {
		return cardMinionRepository.findAllByDeck(deck);
	}

	@GetMapping(path="/deckSpell")
	public @ResponseBody Iterable<CardSpell> getSpellByDeck(@RequestParam String deck) {
		return cardSpellRepository.findAllByDeck(deck);
	}
}