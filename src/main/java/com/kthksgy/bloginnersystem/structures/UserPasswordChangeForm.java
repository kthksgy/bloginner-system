package com.kthksgy.bloginnersystem.structures;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.kthksgy.bloginnersystem.helpers.PasswordHelper;

import lombok.Value;

@Value
public class UserPasswordChangeForm implements PasswordHelper {
	private String username;
	@NotEmpty
	private String password;
	@NotBlank
	@Size(min=MIN_PASSWORD_LENGTH, max=MAX_PASSWORD_LENGTH)
	@Pattern(regexp = PASSWORD_PATTERN)
	private String newPassword;
}
