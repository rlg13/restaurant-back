package com.demo.restaurant.rest.api.utils;

public class Constants {

	private Constants() {
	}

	public static final String HEADER_SESSION = "Authorization";

	public static final Integer MILISECONDS_TO_EXPIRE = 300000;

	public static final String INTERCEPTOR_EXCEPTION_MESSAGE = "Session Invalid";

	public static final String USER_NOT_ALLOWED_OPERATION = "Operation not allowed for the user";

}
