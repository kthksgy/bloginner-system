package com.kthksgy.bloginnersystem;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties("bloginner")
public class BloginnerProperties {
	private String title = "BLOGINNER SITE";
	private String passphrase = "secret";
	
	private boolean resetAdminPassword = false;
	private boolean addDummyData = false;
}
