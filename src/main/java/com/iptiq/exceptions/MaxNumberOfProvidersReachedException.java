package com.iptiq.exceptions;

public class MaxNumberOfProvidersReachedException extends Exception{
	public MaxNumberOfProvidersReachedException(String errorMessage) {
		super("Maximum number of providers was reached for this load balancer" + errorMessage);
	}

	public MaxNumberOfProvidersReachedException() {
		super("Maximum number of providers was reached for this load balancer");
	}
}
