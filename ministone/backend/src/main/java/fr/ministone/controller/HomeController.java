package fr.ministone.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Wesh
 *
 */
@Controller
@RequestMapping("/home")
public class HomeController {
	@GetMapping
	public String home() {
		return "forward:/index.html";
	}
}
