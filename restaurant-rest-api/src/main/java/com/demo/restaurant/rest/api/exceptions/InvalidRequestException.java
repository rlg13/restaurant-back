package com.demo.restaurant.rest.api.exceptions;


public class InvalidRequestException extends Exception {

	private static final long serialVersionUID = -1981753242849508629L;
	
	public static final String INVALID_PARAMS = "Invalid request params";


	public InvalidRequestException(String message) {
		super(message);
	}
}
