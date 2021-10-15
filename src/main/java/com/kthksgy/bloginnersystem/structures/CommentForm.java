package com.kthksgy.bloginnersystem.structures;

import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class CommentForm {
	@NotNull
	private UUID articleId;
	@NotBlank
	private String handlename;
	@NotBlank
	private String content;
}
