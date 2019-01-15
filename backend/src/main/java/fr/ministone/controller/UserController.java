package fr.ministone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.ministone.User;
import fr.ministone.repository.UserRepository;

@Controller
@RequestMapping(path="/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping(path="/getUser") // Map ONLY GET Requests
	public @ResponseBody User getCardMinionByName (@RequestParam String name) {
		return userRepository.findByName(name);
    }
}