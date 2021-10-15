package com.kthksgy.bloginnersystem.controllers;

import java.util.List;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kthksgy.bloginnersystem.entities.Article;
import com.kthksgy.bloginnersystem.entities.Comment;
import com.kthksgy.bloginnersystem.entities.User;
import com.kthksgy.bloginnersystem.exceptions.NotPermittedException;
import com.kthksgy.bloginnersystem.exceptions.ResourceNotFoundException;
import com.kthksgy.bloginnersystem.security.MyLoginUser;
import com.kthksgy.bloginnersystem.services.ArticleService;
import com.kthksgy.bloginnersystem.services.CommentService;
import com.kthksgy.bloginnersystem.services.UserService;
import com.kthksgy.bloginnersystem.structures.CommentForm;

@RestController
public class CommentController {
	@Autowired
	CommentService commentService;
	@Autowired
	UserService userService;
	@Autowired
	ArticleService articleService;
	
	@GetMapping("/api/comments")
	public Page<Comment> getAll(
			@RequestParam(name = "username", required = false) String username,
			Pageable pageable) {
		if(username != null) {
			User user = userService.get(username).orElseThrow(() -> new ResourceNotFoundException("指定したユーザーは存在しません。"));
			List<Article> articles = articleService.getAll(user);
			return commentService.getAll(articles, pageable);
		}
		return commentService.getAll(pageable);
	}
	
	@GetMapping("/api/article-comments")
	public List<Comment> getArticleComments(
			@RequestParam(name = "id", required = true) UUID id) {
		Article article = articleService.get(id).orElseThrow(() -> new ResourceNotFoundException("指定した記事は存在しません。"));
		List<Comment> comments = commentService.getAll(article);
		comments.forEach(comment -> {
			if(!comment.getIsPublished()) {
				comment.setHandlename("");
				comment.setContent("");
			}
		});
		return comments;
	}
	
	@PostMapping("/api/comment/post")
	public void postComment(
			@Valid CommentForm form, BindingResult bindingResult) throws BindException {
		if(bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}
		Article article = articleService.get(form.getArticleId()).orElseThrow(() -> new ResourceNotFoundException("指定した記事は存在しません。"));
		Comment comment = new Comment(article, form.getHandlename(), form.getContent());
		commentService.save(comment);
	}
	
	@PutMapping("/api/comment/is-published")
	public void changeIsPublished(
			@AuthenticationPrincipal MyLoginUser loginUser,
			@RequestParam(name = "id", required = true) UUID id,
			@RequestParam(name = "isPublished", required = true) Boolean isPublished) throws BindException {
		Comment comment = commentService.get(id).orElseThrow(() -> new ResourceNotFoundException("指定したコメントは存在しません。"));
		User articleAuthor = comment.getArticle().getUser();
		if(!loginUser.getUsername().equals(articleAuthor.getUsername()) && !loginUser.hasRole("ADMINISTRATOR")) {
			throw new NotPermittedException();
		}
		comment.setIsPublished(isPublished);
		commentService.save(comment);
	}
	
	@DeleteMapping("/api/comment/delete")
	public void delete(
			@AuthenticationPrincipal MyLoginUser loginUser,
			@RequestParam(name = "id", required = true) UUID id) {
		Comment comment = commentService.get(id).orElseThrow(() -> new ResourceNotFoundException("指定したコメントは存在しません。"));
		User articleAuthor = comment.getArticle().getUser();
		if(!loginUser.getUsername().equals(articleAuthor.getUsername()) && !loginUser.hasRole("ADMINISTRATOR")) {
			throw new NotPermittedException();
		}
		commentService.delete(comment);
	}
}
