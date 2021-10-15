package com.kthksgy.bloginnersystem.controllers;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kthksgy.bloginnersystem.entities.Article;
import com.kthksgy.bloginnersystem.entities.User;
import com.kthksgy.bloginnersystem.exceptions.NotPermittedException;
import com.kthksgy.bloginnersystem.exceptions.ResourceNotFoundException;
import com.kthksgy.bloginnersystem.security.MyLoginUser;
import com.kthksgy.bloginnersystem.services.ArticleService;
import com.kthksgy.bloginnersystem.services.UserService;
import com.kthksgy.bloginnersystem.structures.ArticleForm;

@RestController
public class ArticleController {
	@Autowired
	UserService userService;
	
	@Autowired
	ArticleService service;

	@GetMapping("/api/articles")
	public Page<Article> getAll(
			@RequestParam(name = "username", required = false) String username,
			@RequestParam(name = "keyword", required = false) String keyword,
			Pageable pageable) {
		User user = null;
		if(username != null) {
			user = userService.get(username).orElseThrow();
		}
		return service.getAll(user, keyword, pageable);
	}
	
	@GetMapping("/api/article/{id}")
	public Article get(@PathVariable("id") UUID id) {
		return service.get(id).orElseThrow();
	}
	
	@PostMapping("/api/article/post")
	public void post(@AuthenticationPrincipal MyLoginUser user, @Valid ArticleForm articleForm, BindingResult bindingResult) throws BindException {
		if (bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}
		Article article = new Article(user.getUser(), articleForm.getTitle(), articleForm.getOverview(), articleForm.getThumbnail(), articleForm.getContent());
		service.save(article);
	}
	
	@PutMapping("/api/article/update")
	public void update(
			@AuthenticationPrincipal MyLoginUser user,
			@RequestParam(name = "id", required = true) UUID id,
			@Valid ArticleForm form, BindingResult bindingResult) throws BindException {
		if (bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}
		Article article = service.get(id).orElseThrow(() -> new ResourceNotFoundException("指定した記事は存在しません。"));
		if(!article.getUser().getUsername().equals(user.getUsername()) && !user.hasRole("ADMINISTRATOR")) {
			throw new NotPermittedException();
		}
		if(form.getTitle() != null) {
			article.setTitle(form.getTitle());
		}
		if(form.getOverview() != null) {
			article.setOverview(form.getOverview());
		}
		if(form.getThumbnail() != null) {
			article.setThumbnail(form.getThumbnail());
		}
		if(form.getContent() != null) {
			article.setContent(form.getContent());
		}
		service.save(article);
	}

	@DeleteMapping("/api/article/remove")
	public void remove(@AuthenticationPrincipal MyLoginUser user, @RequestParam(name = "id", required = true) UUID id) {
		Article article = service.get(id).orElseThrow();
		if(!user.hasRole("ADMINISTRATOR") && !article.getUser().getUsername().equals(user.getUsername())) {
			throw new NotPermittedException();
		}
		service.remove(article);
	}

}
