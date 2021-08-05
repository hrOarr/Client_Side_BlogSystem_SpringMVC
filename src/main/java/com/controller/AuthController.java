package com.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.User;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Controller
@RequestMapping("/auth")
public class AuthController {
	
	private static String auth_url = "http://localhost:8081/SpringMVC_CRUD/api/v1/auth";
	
	@ModelAttribute("user")
	public User getUser() {
		return new User(); 
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		return "auth/login_form";
	}
	
	@PostMapping("/login")
	public String userLogin(@ModelAttribute("user") User user, HttpSession session, Model model) throws JsonProcessingException {
		// jackson json conversion from object
		String jsonUser = new ObjectMapper().writeValueAsString(user);
		
		Client client = Client.create();
		WebResource resource = client.resource(auth_url);
		ClientResponse response = resource.path("/login")
				                  .type("application/json").post(ClientResponse.class, jsonUser);
		String resp = response.getEntity(String.class);
		if(response.getStatus()==400) {
			List<String> errors = new ObjectMapper().readValue(resp, new TypeReference<List<String>>() {});
			model.addAttribute("errors", errors);
			return "auth/login_form";
		}
		session.setAttribute("current_user", new ObjectMapper().readValue(resp, User.class));
		System.out.println(session.getAttribute("current_user"));
		
		return "redirect:/";
	}
	
	@GetMapping("/register")
	public String register(Model model) {
		return "auth/register_form";
	}
	
	@PostMapping("/register")
	public String userRegister(@ModelAttribute("user") User user, HttpSession session, Model model) throws JsonProcessingException {
		// jackson json conversion from object
		String jsonUser = new ObjectMapper().writeValueAsString(user);
		Client client = Client.create();
		WebResource resource = client.resource(auth_url);
		ClientResponse response = resource.path("/register")
				                  .type("application/json").post(ClientResponse.class, jsonUser);
		String resp = response.getEntity(String.class);
		if(response.getStatus()==400) {
			List<String> errors = new ObjectMapper().readValue(resp, new TypeReference<List<String>>() {});
			model.addAttribute("errors", errors);
			return "auth/register_form";
		}
		session.setAttribute("current_user", new ObjectMapper().readValue(resp, User.class));
		System.out.println(session.getAttribute("current_user"));
		
		return "redirect:/";
	}
	
	@GetMapping("/logout")
	public String logout(Model model, HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
}
