package com.demo.restaurant.restaurantbatch.job.exception;

public class ProcessException extends Exception {

	private static final long serialVersionUID = 3757257120928950876L;

	public static final String ERROR_PROCESS = "Error Server processing order %d";
	public ProcessException(String message) {
		super(message);
	}
}
