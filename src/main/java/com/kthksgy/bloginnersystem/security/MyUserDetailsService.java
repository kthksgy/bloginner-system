package com.kthksgy.bloginnersystem.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kthksgy.bloginnersystem.entities.User;
import com.kthksgy.bloginnersystem.repositories.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) {
		Optional<User> user = userRepository.findByUsername(username);
		return user.map(MyLoginUser::new).orElseThrow(() -> new UsernameNotFoundException("指定したユーザー名(" + username + ")のユーザーは存在しません。"));
    }
}
