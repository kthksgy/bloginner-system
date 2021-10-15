package com.kthksgy.bloginnersystem.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public class DummyException extends RuntimeException {
	@Getter
	private HttpStatus status;
	
	public DummyException(HttpStatus status, String msg) {
		super(msg);
		this.status = status;
	}
	
	public DummyException(HttpStatus status, String msg, Throwable cause) {
		super(msg, cause);
		this.status = status;
	}
}
