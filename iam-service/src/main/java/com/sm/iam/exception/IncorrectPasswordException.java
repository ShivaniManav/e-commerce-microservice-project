package com.sm.iam.exception;

public class IncorrectPasswordException extends RuntimeException {

	public IncorrectPasswordException() {
		super("");
	}

	public IncorrectPasswordException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncorrectPasswordException(String message) {
		super(message);
	}

}
