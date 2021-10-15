package com.kthksgy.bloginnersystem.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.kthksgy.bloginnersystem.entities.User;

public interface UserRepository extends CrudRepository<User, String> {
	Page<User> findAll(Pageable pageable);

	Optional<User> findByUsername(String username);
}