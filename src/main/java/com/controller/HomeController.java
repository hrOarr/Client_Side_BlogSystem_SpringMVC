package com.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.Article;
import com.model.Tag;

@Controller
public class HomeController {
	private static String article_uri = "http://localhost:8081/SpringMVC_CRUD/api/v1/articles";
	private static String tag_uri = "http://localhost:8081/SpringMVC_CRUD/api/v1/tags";
	
	@Autowired
	public HomeController() {}
	
	@GetMapping()
	public String getHome(Model model) throws JsonMappingException, JsonProcessingException {
		List<Article> articles = new ArrayList<Article>();
		List<Tag> tags = new ArrayList<Tag>();
		
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<String> responseArticle = restTemplate.getForEntity(article_uri, String.class);
		
		ObjectMapper objectMapper = new ObjectMapper();
		articles = objectMapper.readValue(responseArticle.getBody(), new TypeReference<List<Article>>() {});
		
		ResponseEntity<String> responseTag = restTemplate.getForEntity(tag_uri, String.class);
		tags = objectMapper.readValue(responseTag.getBody(), new TypeReference<List<Tag>>() {});
		
		model.addAttribute("articles", articles);
		model.addAttribute("tags", tags);
		return "home";
	}

}
