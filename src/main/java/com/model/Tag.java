package com.model;

import java.util.ArrayList;
import java.util.List;

public class Tag {
	private int id;
	private String name;
	private List<Article> articles = new ArrayList<Article>();
	
	public Tag() {
	}
	public Tag(String name) {
		this.name = name;
	}
	public Tag(int id, String name, List<Article> articles) {
		this.id = id;
		this.name = name;
		this.articles = articles;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}
	
}
