package com.kthksgy.bloginnersystem.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kthksgy.bloginnersystem.entities.Article;
import com.kthksgy.bloginnersystem.entities.User;
import com.kthksgy.bloginnersystem.repositories.ArticleRepository;

@Service
public class ArticleService {
	@Autowired
	ArticleRepository repo;

	@Transactional(readOnly = true)
	public Optional<Article> get(UUID id) {
		return repo.findById(id);
	}

	@Transactional(readOnly = true)
	public Page<Article> getAll(Pageable pageable) {
		return repo.findAllByOrderByCreatedAtDesc(pageable);
	}

	@Transactional(readOnly = true)
	public Page<Article> getAll(User user, Pageable pageable) {
		return repo.findByUserOrderByCreatedAtDesc(user, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Article> getAll(String title, Pageable pageable) {
		return repo.findByTitleContainingOrderByCreatedAtDesc(title, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Article> getAll(User user, String title, Pageable pageable) {
		if (user == null && title == null) {
			return repo.findAllByOrderByCreatedAtDesc(pageable);
		} else if (user == null) {
			return repo.findByTitleContainingOrderByCreatedAtDesc(title, pageable);
		} else if (title == null) {
			return repo.findByUserOrderByCreatedAtDesc(user, pageable);
		} else {
			return repo.findByUserAndTitleContainingOrderByCreatedAtDesc(user, title, pageable);
		}
	}

	@Transactional(readOnly = true)
	public List<Article> getAll(User user) {
		return repo.findByUserOrderByCreatedAtDesc(user);
	}

	@Transactional(readOnly = true)
	public long count() {
		return repo.count();
	}

	@Transactional(readOnly = false)
	public void save(Article article) {
		repo.save(article);
	}

	@Transactional(readOnly = false)
	public void remove(Article article) {
		repo.delete(article);
	}
}
