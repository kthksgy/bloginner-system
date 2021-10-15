package com.kthksgy.bloginnersystem.structures;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.kthksgy.bloginnersystem.entities.User;
import com.kthksgy.bloginnersystem.helpers.PasswordHelper;

import lombok.Value;

@Value
public class UserRegisterForm implements PasswordHelper {
	@NotBlank
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message = "英数字のみ使用できます。")
	private String username;

	@NotBlank
	@Size(min = MIN_PASSWORD_LENGTH, max = MAX_PASSWORD_LENGTH, message = "8～32文字で指定してください。")
	@Pattern(regexp = PASSWORD_PATTERN, message = "英数字と記号.?/-のみ使用できます。")
	private String password;

	private Integer restriction;

	public User toUser() {
		User user = new User(this.username, this.password);
		if (this.restriction != null) {
			user.setRestriction(this.restriction);
		}
		return user;
	}
}
