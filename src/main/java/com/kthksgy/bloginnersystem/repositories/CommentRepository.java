package com.kthksgy.bloginnersystem.repositories;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.kthksgy.bloginnersystem.entities.Article;
import com.kthksgy.bloginnersystem.entities.Comment;

public interface CommentRepository extends CrudRepository<Comment, UUID> {
	Page<Comment> findAllByOrderByCreatedAtDesc(Pageable pageable);
	List<Comment> findByArticleOrderByCreatedAtDesc(Article article);
	Page<Comment> findByArticleInOrderByCreatedAtDesc(Collection<Article> articles, Pageable pageable);
}
