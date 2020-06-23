package com.iptiq.exceptions;

public class NoAvailableProvidersException extends Exception {


	public NoAvailableProvidersException(String errorMessage) {
		super("Maximum number of requests for available providers was reached" + errorMessage);
	}

	public NoAvailableProvidersException() {
		super("Maximum number of requests for available providers was reached");
	}
}
