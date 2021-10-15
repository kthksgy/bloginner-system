package com.kthksgy.bloginnersystem.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.kthksgy.bloginnersystem.entities.Article;
import com.kthksgy.bloginnersystem.entities.User;

public interface ArticleRepository extends CrudRepository<Article, UUID> {
	// findAll "By" Order...とWHERE句無し(findAll等)の場合はByが必要
	Page<Article> findAllByOrderByCreatedAtDesc(Pageable pageable);
	
	List<Article> findByUserOrderByCreatedAtDesc(User user);
	Page<Article> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
	Page<Article> findByUserAndTitleContainingOrderByCreatedAtDesc(User user, String title, Pageable pageable);
	
	Page<Article> findByTitleContainingOrderByCreatedAtDesc(String title, Pageable pageable);

//	Optional<Article> findById(UUID id);
}
