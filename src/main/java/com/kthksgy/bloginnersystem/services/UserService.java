package com.kthksgy.bloginnersystem.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kthksgy.bloginnersystem.entities.User;
import com.kthksgy.bloginnersystem.helpers.PasswordHelper;
import com.kthksgy.bloginnersystem.repositories.UserRepository;

@Service
public class UserService {
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;
	
	@Transactional(readOnly = true)
	public long count() {
		return userRepository.count();
	}

	@Transactional(readOnly = true)
	public Page<User> getAll(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	@Transactional(readOnly = true)
	public Optional<User> get(String username) {
		return userRepository.findByUsername(username);
	}

	@Transactional(readOnly = false)
	public void save(String username, String password) {
		User user = new User(username, password);
		save(user);
	}

	@Transactional(readOnly = false)
	public void save(User user) {
		if (!user.getPassword().matches(PasswordHelper.ENCODED_PASSWORD_PREFIX)) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		userRepository.save(user);
	}
	
	public boolean checkPassword(User user, String password) {
		if (!user.getPassword().matches(PasswordHelper.ENCODED_PASSWORD_PREFIX)) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		return passwordEncoder.matches(password, user.getPassword());
	}
	
	@Transactional(readOnly = false)
	public void remove(User user) {
		userRepository.delete(user);
	}
}
