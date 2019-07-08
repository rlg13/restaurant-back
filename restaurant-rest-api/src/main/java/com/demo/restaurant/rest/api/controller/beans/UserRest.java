package com.demo.restaurant.rest.api.controller.beans;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRest {

	private Long id;
	private String name;	
	private String password;
	private String sessionId;
	
}
