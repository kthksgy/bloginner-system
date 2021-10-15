package com.kthksgy.bloginnersystem.exceptions;

import org.springframework.security.core.AuthenticationException;

public class JWTException extends AuthenticationException {
	public JWTException(String msg) {
		super(msg);
	}
	
	public JWTException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
