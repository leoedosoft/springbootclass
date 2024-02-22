package com.teoresi.webapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {
	
	
	@GetMapping("/ciao")
	public String helloCiao() {
		return "ciao_tutti";
	}

}
