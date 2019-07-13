package com.demo.restaurant.rest.api.exceptions;

import com.demo.restaurant.rest.api.types.OrderState;

public class IlegalProcessStateException extends Exception {

	private static final long serialVersionUID = 2949483063495181143L;
	private static final String ILEGAL_TRANSITION = "Transition from state %s to %s not allowedCUser Exists";

	public static final String ILEGAL_CANCELATION = "Cancellation of past orders is not allowed";
	public static final String ILEGAL_USER_TO_ACTION = "User without privileges to perform the action";

	public IlegalProcessStateException(OrderState actualState, OrderState newState) {
		super(String.format(ILEGAL_TRANSITION, actualState, newState));
	}

	public IlegalProcessStateException(String message) {
		super(message);
	}
}
