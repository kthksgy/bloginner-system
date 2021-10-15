package com.kthksgy.bloginnersystem.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kthksgy.bloginnersystem.exceptions.DummyException;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class TestController {
	//	@GetMapping("/api/test")
	//	public String greeting(@AuthenticationPrincipal(expression = "user") MyLoginUser user, CsrfToken csrfToken) {
	//		log.debug("token : {}", csrfToken.getToken());
	//		log.debug("access user : {}", user.toString());
	//		return String.format("XSRF-TOKEN: %s, User: %s", csrfToken.getToken(), user.toString());
	//	}

	@GetMapping("/api/test/hello-world")
	public String helloWorld() {
		return "Hello World";
	}

	@PostMapping("/api/test/echo")
	public String echo(@RequestParam(name = "message") String message) {
		return String.format("{\"message\":\"%s\"}", message);
	}

	@GetMapping("/api/test/error400")
	public String error400() {
		throw new DummyException(HttpStatus.BAD_REQUEST, "リクエストが不正です。");
	}

	@GetMapping("/api/test/error401")
	public String error401() {
		throw new DummyException(HttpStatus.UNAUTHORIZED, "認証が必要です。");
	}

	@GetMapping("/api/test/error402")
	public String error402() {
		throw new DummyException(HttpStatus.PAYMENT_REQUIRED, "支払いが必要です。");
	}

	@GetMapping("/api/test/error403")
	public String error403() {
		throw new DummyException(HttpStatus.FORBIDDEN, "リソースへのアクセスは禁止されています。");
	}

	@GetMapping("/api/test/error404")
	public String error404() {
		throw new DummyException(HttpStatus.NOT_FOUND, "見つかりませんでした。");
	}

	@GetMapping("/api/test/error500")
	public String error500() {
		throw new DummyException(HttpStatus.INTERNAL_SERVER_ERROR, "サーバー内部エラーが発生しました。");
	}

	@GetMapping("/test/{message}")
	public String greeting(@PathVariable(name = "message") String message) {
		return "hello " + message;
	}

	@PostMapping("/test")
	public String postGreeting(@RequestParam(name = "message") String message) {
		return "hello " + message;
	}

	@RequestMapping("/resource")
	public Map<String, Object> home() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("id", UUID.randomUUID().toString());
		model.put("content", "Hello World");
		return model;
	}
}
