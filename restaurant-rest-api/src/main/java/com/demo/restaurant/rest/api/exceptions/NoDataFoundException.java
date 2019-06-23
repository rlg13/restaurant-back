package com.demo.restaurant.rest.api.exceptions;


public class NoDataFoundException extends Exception {

	
	public static final String USER_NOT_FOUND = "User not found";
	public static final String DISH_NOT_FOUND = "Dish not found";
	

	public NoDataFoundException(String message) {
		super(message);
	}
}
