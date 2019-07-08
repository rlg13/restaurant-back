package com.demo.restaurant.rest.api.exceptions;


public class NoDataFoundException extends Exception {
	
	private static final long serialVersionUID = 2939507332245372270L;
	
	public static final String USER_NOT_FOUND = "User not found";
	public static final String DISH_NOT_FOUND = "Dish not found";
	public static final String ORDER_NOT_FOUND = "Order not found";
	public static final String SESSION_NOT_FOUND = "Session not found";
	

	public NoDataFoundException(String message) {
		super(message);
	}
}
