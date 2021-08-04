package com.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.Article;
import com.model.ArticleDTO;
import com.model.Tag;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.utility.Convertor;

@Controller
@RequestMapping("/articles")
public class ArticleController {
	
	private static String article_uri = "http://localhost:8081/SpringMVC_CRUD/api/v1/articles";
	private static String tag_uri = "http://localhost:8081/SpringMVC_CRUD/api/v1/tags";
	
	private static Convertor convertor = new Convertor();
	
	public ArticleController() {}
	
	@GetMapping()
	public String allArticles(Model model) throws JsonMappingException, JsonProcessingException {
		List<Article> articles = new ArrayList<Article>();
		List<Tag> tags = new ArrayList<Tag>();
		
//		RestTemplate restTemplate = new RestTemplate();
//		
//		ResponseEntity<String> responseArticle = restTemplate.getForEntity(article_uri, String.class);
//		
//		ObjectMapper objectMapper = new ObjectMapper();
//		articles = objectMapper.readValue(responseArticle.getBody(), new TypeReference<List<Article>>() {});
//		
//		ResponseEntity<String> responseTag = restTemplate.getForEntity(tag_uri, String.class);
//		tags = objectMapper.readValue(responseTag.getBody(), new TypeReference<List<Tag>>() {});
		
		Client client = Client.create();
		
		// article request
		WebResource resource = client.resource(article_uri);
		ClientResponse response = resource.accept("application/json").get(ClientResponse.class);
		
		String responseArticle = response.getEntity(String.class);
		ObjectMapper objectMapper = new ObjectMapper();
		articles = objectMapper.readValue(responseArticle, new TypeReference<List<Article>>() {});
		
		// tag request
		resource = client.resource(tag_uri);
		response = resource.accept("application/json").get(ClientResponse.class);
		
		String responseTag = response.getEntity(String.class);
		tags = objectMapper.readValue(responseTag, new TypeReference<List<Tag>>() {});
		
		model.addAttribute("articles", articles);
		model.addAttribute("tags", tags);
		return "articles/article_list";
	}
	
	// get single article
	@GetMapping("/{id}")
	public String showArticle(@PathVariable("id") int id, Model model) {
		try {
//			RestTemplate restTemplate = new RestTemplate();
//			
//			ResponseEntity<String> response = restTemplate.getForEntity(article_uri+"/"+id, String.class);
//			
//			ObjectMapper objectMapper = new ObjectMapper();
//			Article article = objectMapper.readValue(response.getBody(), Article.class);
			
			Client client = Client.create();
			WebResource resource = client.resource(article_uri);
			ClientResponse response = resource
					             .path("/"+id)
					             .accept("application/json").get(ClientResponse.class);
			
			String responseArticle = response.getEntity(String.class);
			Article article = new ObjectMapper().readValue(responseArticle, Article.class);
			
			model.addAttribute("article", article);
			return "articles/show_article";
		} catch (Exception e) {
			model.addAttribute("msg", "Resource Not Found");
			return "error";
		}
	}
	
	// get article form
	@GetMapping("/add")
	public String addArticle(Model model) {
		model.addAttribute("type", "add");
//		model.addAttribute("article", new Article());
		return "articles/show_form";
	}
	
	@ModelAttribute("articleDTO")
	public ArticleDTO getArticle() {
		return new ArticleDTO();
	}
	
	// add article
	@PostMapping("/add")
	public String addArticle(@ModelAttribute("articleDTO") ArticleDTO articleDTO, Model model) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		// jackson json conversion from object
		String jsonArticleDTO = new ObjectMapper().writeValueAsString(articleDTO);
		//System.out.println(jsonArticleDTO);
		
		// implicit conversion of object to json with headers
		//HttpEntity<String> entity = new HttpEntity<>(jsonArticleDTO, headers);
		//System.out.println(entity);
		Client client = Client.create();
		WebResource resource = client.resource(article_uri);
		ClientResponse response = resource.path("/add").type("application/json").post(ClientResponse.class, jsonArticleDTO);
				
		if(response.getStatus()==400) {
			String resp = response.getEntity(String.class);
			List<String> errors = new ObjectMapper().readValue(resp, new TypeReference<List<String>>() {});
			model.addAttribute("errors", errors);
			model.addAttribute("type", "add");
			return "articles/show_form";
		}
		
		return "redirect:/articles";
	}
	
	// get edit article form
	@GetMapping("/edit/{id}")
	public String editArticle(@PathVariable("id") int id, Model model) {
		try {
			
//			RestTemplate restTemplate = new RestTemplate();
//			ResponseEntity<String> response = restTemplate.getForEntity(article_uri+"/"+id, String.class);
//			
//			ObjectMapper objectMapper = new ObjectMapper();
//			Article article = objectMapper.readValue(response.getBody(), Article.class);
			Client client = Client.create();
			WebResource resource = client.resource(article_uri);
			ClientResponse response = resource.path("/"+id).accept("application/json").get(ClientResponse.class);
			
			String responseArticle = response.getEntity(String.class);
			Article article = new ObjectMapper().readValue(responseArticle, Article.class);
			
			model.addAttribute("articleDTO", convertor.articleToArticleDTO(article));
			model.addAttribute("type", "edit");
			return "articles/show_form";
		} catch (Exception e) {
			model.addAttribute("msg", "Unauthorized...");
			return "error";
		}
	}
	
	// edit article
	@PostMapping("/edit")
	public String editArticle(@ModelAttribute("articleDTO") ArticleDTO articleDTO, BindingResult result, Model model) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		// jackson json conversion from object
		String jsonArticleDTO = new ObjectMapper().writeValueAsString(articleDTO);
		
		// implicit conversion of object to json with headers
		//HttpEntity<ArticleDTO> entity = new HttpEntity<>(articleDTO, headers);
		
		Client client = Client.create();
		WebResource resource = client.resource(article_uri);
		ClientResponse response = resource.path("/edit").type("application/json").put(ClientResponse.class, jsonArticleDTO);
		
		if(response.getStatus()==400) {
			String resp = response.getEntity(String.class);
			List<String> errors = new ObjectMapper().readValue(resp, new TypeReference<List<String>>() {});
			model.addAttribute("errors", errors);
			model.addAttribute("type", "edit");
			return "articles/show_form";
		}
		
		return "redirect:/articles";
	}
	
	// get articles by tag
	@GetMapping("/byTag")
	public String getArticlesByTag(@RequestParam("tag") String name, Model model) throws JsonMappingException, JsonProcessingException {
		
//		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(article_uri+"/byTag").queryParam("tag", name);
//		
//		RestTemplate restTemplate = new RestTemplate();
//		ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);
//		
//		ObjectMapper objectMapper = new ObjectMapper();
//		List<Article> articles = objectMapper.readValue(response.getBody(), new TypeReference<List<Article>>() {});
//		
//		ResponseEntity<String> responseTag = restTemplate.getForEntity(tag_uri, String.class);
//		List<Tag> tags = objectMapper.readValue(responseTag.getBody(), new TypeReference<List<Tag>>() {});
		Client client = Client.create();
		WebResource resource = client.resource(article_uri);
		ClientResponse response = resource.path("/byTag")
				                 .queryParam("tag", name)
				                 .accept("application/json").get(ClientResponse.class);
		
		String responseArticle = response.getEntity(String.class);
		List<Article> articles = new ObjectMapper().readValue(responseArticle, new TypeReference<List<Article>>() {});
		
		resource = client.resource(tag_uri);
		response = resource.accept("application/json").get(ClientResponse.class);
		
		String responseTag = response.getEntity(String.class);
		List<Tag> tags = new ObjectMapper().readValue(responseTag, new TypeReference<List<Tag>>() {});
		
		model.addAttribute("articles", articles);
		model.addAttribute("tags", tags);
		model.addAttribute("tag_name", name);
		return "articles/article_list_by_tags";
	}
	
	@PostMapping("/delete/{id}")
	public String deleteArticle(@PathVariable("id") int id, Model model) {
		
//		Map<String, Integer> params = new HashMap<String, Integer>();
//		params.put("id", id);
//		
//		RestTemplate restTemplate = new RestTemplate();
//		restTemplate.delete(article_uri+"/delete/{id}", params);
		
		Client client = Client.create();
		WebResource resource = client.resource(article_uri);
		ClientResponse response = resource.path("/delete/"+id).delete(ClientResponse.class);
		
		if(response.getStatus()!=200) {
			//
		}
		
		return "redirect:/articles";
	}
}
