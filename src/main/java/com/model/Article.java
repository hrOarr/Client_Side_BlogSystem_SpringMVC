package com.model;

import java.util.ArrayList;
import java.util.List;

public class Article {
	
	private int id;
	private String title;
	private String body;
	private User user;
	private List<Tag> tags = new ArrayList<Tag>();

	public Article() {
	}

	public Article(int id, String title, String body, User user, List<Tag> tags) {
		this.id = id;
		this.title = title;
		this.body = body;
		this.user = user;
		this.tags = tags;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		return "Article [id=" + id + ", title=" + title + ", body=" + body + ", user=" + user + ", tags=" + tags + "]";
	}
	
}