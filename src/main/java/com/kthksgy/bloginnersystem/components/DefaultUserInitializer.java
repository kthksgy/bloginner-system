package com.kthksgy.bloginnersystem.components;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kthksgy.bloginnersystem.BloginnerProperties;
import com.kthksgy.bloginnersystem.entities.Article;
import com.kthksgy.bloginnersystem.entities.Comment;
import com.kthksgy.bloginnersystem.entities.User;
import com.kthksgy.bloginnersystem.helpers.PasswordHelper;
import com.kthksgy.bloginnersystem.services.ArticleService;
import com.kthksgy.bloginnersystem.services.CommentService;
import com.kthksgy.bloginnersystem.services.UserService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DefaultUserInitializer {
	@Autowired
	UserService userService;

	@Autowired
	ArticleService articleService;
	
	@Autowired
	CommentService commentService;

	@Autowired
	BloginnerProperties bloginnerProperties;

	@PostConstruct
	public void postConstruct() {
		//		String adminPassword = PasswordHelper.generatePassword(PasswordHelper.MAX_PASSWORD_LENGTH);
		Optional<User> optUser = userService.get("admin");
		User user = null;
		log.warn("【管理者】ユーザー名: {}", "admin");
		if (optUser.isEmpty() || bloginnerProperties.isResetAdminPassword()) {
			user = optUser.orElse(new User("admin", "admin"));
			user.setPassword("admin");
			log.warn("【管理者】パスワード: {}", user.getPassword());
		} else {
			user = optUser.get();
		}
		user.setRestriction(0);

		userService.save(user);

		if (bloginnerProperties.isAddDummyData()) {
			log.info("ダミーデータを追加します。");
			user = userService.get("admin").orElseThrow();
			for (int i = 0; i < 100; i++) {
				String suffix = String.format("%3d", i);
				Article article = new Article(
						user,
						String.format("タイトル %s %s", suffix, PasswordHelper.generatePassword(5, 0, 10000)),
						String.format("概要です。 %s %s", suffix, PasswordHelper.generatePassword(30, 0, 10000)),
						"",
						String.format("本文です。 %s %s", suffix, PasswordHelper.generatePassword(1000, 0, 10000)));
				articleService.save(article);
			}
			
			List<Article> articles = articleService.getAll(user);
			for(Article article : articles) {
				for(int i = 0; i < 5; i++) {
					Comment comment = new Comment(
							article,
							PasswordHelper.generatePassword(10, 0, 10000),
							PasswordHelper.generatePassword(200, 0, 10000));
					commentService.save(comment);
				}
			}
		}

	}
}
