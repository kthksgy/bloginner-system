package com.kthksgy.bloginnersystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kthksgy.bloginnersystem.BloginnerProperties;
import com.kthksgy.bloginnersystem.structures.SiteInformation;

@RestController
public class CommonController {
	@Autowired
	BloginnerProperties bloginnerProperties;

	@GetMapping("/api/site-information")
	public SiteInformation getSiteInformation() {
		return new SiteInformation(bloginnerProperties.getTitle());
	}
}
