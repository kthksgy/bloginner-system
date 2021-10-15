package com.kthksgy.bloginnersystem.exceptions;

public class NotPermittedException extends RuntimeException {
	
	public NotPermittedException() {
		super("この操作は許可されていません。");
	}
}
