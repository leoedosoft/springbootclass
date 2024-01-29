package com.teoresi.blogwebapp.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.teoresi.blogwebapp.repository.IUserRepository;

@Controller
public class CommonController {
	
	@Autowired
	private IUserRepository userRepo;

	@GetMapping(value="index")
	public String getHome(Model model, HttpServletRequest request) {
		model.addAttribute("pageTitle", "Home");
		return "index";
	}
	
	@GetMapping(value="/")
	public String getHome2(Model model, HttpServletRequest request) {
		model.addAttribute("pageTitle", "Home");
		return "index";
	}
	
	
	@RequestMapping("/work"	)
	public String getWork() {
		return "work";
	}
	
	@RequestMapping("/about"	)
	public String getAbout() {
		return "about";
	}

	
	@RequestMapping("/pricing"	)
	public String getPricing() {
		return "pricing";
	}
	
	@RequestMapping("/contact"	)
	public String getContact() {
		return "contact";
	}
	

}
