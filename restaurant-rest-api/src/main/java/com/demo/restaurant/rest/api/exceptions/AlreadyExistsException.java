package com.demo.restaurant.rest.api.exceptions;

public class AlreadyExistsException extends Exception {

	private static final long serialVersionUID = 8620529608577123649L;

	public static final String USER_EXISTS = "User Exists";
	public static final String DISH_EXISTS = "Dish Exists";

	public AlreadyExistsException(String message) {
		super(message);
	}
}
