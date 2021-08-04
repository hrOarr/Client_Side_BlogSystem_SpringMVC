package com.utility;

import java.util.List;

import com.model.Article;
import com.model.ArticleDTO;
import com.model.Tag;

public class Convertor {
	public ArticleDTO articleToArticleDTO(Article article) {
		ArticleDTO articleDTO = new ArticleDTO();
		// set id
		articleDTO.setId(article.getId());
		// set title
		articleDTO.setTitle(article.getTitle());
		// set tags
		List<Tag> tags = article.getTags();
		String str = "";
		if(!tags.isEmpty()) {
			for(Tag tag:tags) {
				str += tag.getName().toLowerCase();
				str += ",";
			}
		}
		articleDTO.setTags(str);
		// set body
		articleDTO.setBody(article.getBody());
		// set user
		articleDTO.setUser_id(article.getUser().getId());
		return articleDTO;
	}
}
