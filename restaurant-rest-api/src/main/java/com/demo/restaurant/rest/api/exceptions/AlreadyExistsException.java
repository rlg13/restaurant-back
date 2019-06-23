package com.demo.restaurant.rest.api.exceptions;


public class AlreadyExistsException extends Exception {

	
	public static final String USER_EXISTS = "User Exists";
	public static final String DISH_EXISTS = "Dish Exists";
	

	public AlreadyExistsException(String message) {
		super(message);
	}
}
