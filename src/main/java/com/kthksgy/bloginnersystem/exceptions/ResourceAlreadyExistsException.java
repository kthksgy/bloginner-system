package com.kthksgy.bloginnersystem.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException {
	public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
