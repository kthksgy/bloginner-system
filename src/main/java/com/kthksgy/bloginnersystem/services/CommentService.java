package com.kthksgy.bloginnersystem.services;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kthksgy.bloginnersystem.entities.Article;
import com.kthksgy.bloginnersystem.entities.Comment;
import com.kthksgy.bloginnersystem.repositories.CommentRepository;

@Service
public class CommentService {
	@Autowired
	CommentRepository commentRepository;

	@Transactional(readOnly = true)
	public Optional<Comment> get(UUID id) {
		return commentRepository.findById(id);
	}
	
	@Transactional(readOnly = true)
	public Page<Comment> getAll(Pageable pageable) {
		return commentRepository.findAllByOrderByCreatedAtDesc(pageable);
	}

	@Transactional(readOnly = true)
	public Page<Comment> getAll(Collection<Article> articles, Pageable pageable) {
		return commentRepository.findByArticleInOrderByCreatedAtDesc(articles, pageable);
	}

	@Transactional(readOnly = true)
	public List<Comment> getAll(Article article) {
		return commentRepository.findByArticleOrderByCreatedAtDesc(article);
	}

	@Transactional(readOnly = false)
	public void save(Comment comment) {
		commentRepository.save(comment);
	}
	
	@Transactional(readOnly = false)
	public void delete(Comment comment) {
		commentRepository.delete(comment);
	}
}
