package com.kthksgy.bloginnersystem.structures;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ArticleForm {
	@NotBlank
	private String title;
	private String overview;
	private String thumbnail;
	private String content;
}
