package com.kthksgy.bloginnersystem.controllers;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;

import com.kthksgy.bloginnersystem.entities.User;
import com.kthksgy.bloginnersystem.exceptions.DummyException;
import com.kthksgy.bloginnersystem.exceptions.ResourceAlreadyExistsException;
import com.kthksgy.bloginnersystem.exceptions.ResourceNotFoundException;
import com.kthksgy.bloginnersystem.security.MyLoginUser;
import com.kthksgy.bloginnersystem.services.UserService;
import com.kthksgy.bloginnersystem.structures.UserPasswordChangeForm;
import com.kthksgy.bloginnersystem.structures.UserRegisterForm;


@RestController
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping("/api/users")
	public Page<User> getAll(Pageable pageable) {
		return userService.getAll(pageable);
	}
	
	@GetMapping("/api/me")
	public User getMyUser(
			/* MyLoginUserのuserプロパティをインジェクション */
			@AuthenticationPrincipal MyLoginUser loginUser) {
		return loginUser.getUser();
	}

	@GetMapping("/api/my-authorities")
	public Set<String> get(
			/* MyLoginUserのuserプロパティをインジェクション */
			@AuthenticationPrincipal MyLoginUser loginUser) {
		return loginUser.getAuthorityStrings();
	}

	@GetMapping("/api/user/{username}")
	public User get(@PathVariable(name = "username", required = true) String username) {
		return userService.get(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@PostMapping("/api/user/register")
	public void register(@Valid UserRegisterForm form, BindingResult bindingResult) throws BindException {
		if(bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}
		if(userService.get(form.getUsername()).isPresent()) {
			throw new ResourceAlreadyExistsException("指定したユーザーは既に存在します。");
		}
		User user = form.toUser();
		userService.save(user);
	}

	@PutMapping("/api/user/change-password")
	public void changePassword(
			@AuthenticationPrincipal MyLoginUser loginUser,
			@Valid UserPasswordChangeForm form,
			BindingResult bindingResult
			) throws BindException {
		if(bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}
		User user = loginUser.hasRole("ADMINISTRATOR") && form.getUsername() != null && form.getUsername().isEmpty() ? userService.get(form.getUsername()).orElseThrow() : loginUser.getUser();
		if(!userService.checkPassword(user, form.getPassword())) {
			throw new DummyException(HttpStatus.UNAUTHORIZED, "現在のパスワードが間違っています。");
		}
		user.setPassword(form.getNewPassword());
		userService.save(user);
	}

	@PutMapping("/api/user/change-restriction")
	public void changeRestriction(
			@RequestParam(name = "username", required = true) String username,
			@RequestParam(name = "restriction", required = true) Integer restriction
		) {
		User user = userService.get(username).orElseThrow();
		user.setRestriction(restriction);
		userService.save(user);
	}

	@DeleteMapping("/api/user/remove")
	public void remove(
			@AuthenticationPrincipal MyLoginUser loginUser,
			@RequestParam(name = "username", required = true) String username) {
		User user = userService.get(username).orElseThrow(() -> new ResourceNotFoundException("指定したユーザーは存在しません。"));
		if(loginUser.getUsername().equals(user.getUsername())) {
			throw new DummyException(HttpStatus.FORBIDDEN, "自分自身を削除する事は出来ません。");
		}
		userService.remove(user);
	}
}
