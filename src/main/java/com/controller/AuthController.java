package com.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.User;

@Controller
@RequestMapping("/auth")
public class AuthController {
	
	private static String url = "http://localhost:8081/SpringMVC_CRUD/api/v1/auth";
	
	@ModelAttribute("user")
	public User getUser() {
		return new User(); 
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		return "auth/login_form";
	}
	
	@PostMapping("/login")
	public String submitLogin(@ModelAttribute("user") User user, HttpSession session) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		// jackson json conversion from object
		//String jsonUser = new ObjectMapper().writeValueAsString(user);
		
		// implicit conversion of object to json with headers
		HttpEntity<User> entity = new HttpEntity<>(user, headers);
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		
		try {
			response = restTemplate.postForEntity(url+"/login", entity, String.class);
			User obj = new ObjectMapper().readValue(response.getBody(), User.class);
			session.setAttribute("current_user", obj);
		}
		catch (Exception e) {
			System.out.println(response);
			return "auth/login_form";
		}
		
		return "redirect:/";
	}
	
	@GetMapping("/register")
	public String register(Model model) {
		return "auth/register_form";
	}
	
	@PostMapping("/register")
	public String submitRegister(@ModelAttribute("user") User user, HttpSession session) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		// jackson json conversion from object
		// String jsonUser = new ObjectMapper().writeValueAsString(user);
		
		// implicit conversion of object to json with headers
		HttpEntity<User> entity = new HttpEntity<>(user, headers);
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		
		try {
			response = restTemplate.postForEntity(url+"/register", entity, String.class);
			User obj = new ObjectMapper().readValue(response.getBody(), User.class);
			session.setAttribute("current_user", obj);
		}
		catch (Exception e) {
			System.out.println(response);
			return "auth/register_form";
		}
		return "redirect:/";
	}
	
	@GetMapping("/logout")
	public String logout(Model model, HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
}
