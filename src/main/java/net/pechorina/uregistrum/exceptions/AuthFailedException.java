package net.pechorina.uregistrum.exceptions;

public class AuthFailedException extends Exception {
	private static final long serialVersionUID = 1L;

	public AuthFailedException(String message) {
		super(message);
	}

}
