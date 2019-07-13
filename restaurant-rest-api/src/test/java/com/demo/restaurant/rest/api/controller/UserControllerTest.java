package com.demo.restaurant.rest.api.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.demo.restaurant.rest.api.controller.beans.UserRest;
import com.demo.restaurant.rest.api.model.Users;
import com.demo.restaurant.rest.api.service.SessionService;
import com.demo.restaurant.rest.api.service.UsersService;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

	@Mock
	private UsersService usersService;

	@Mock
	private SessionService sessionService;
	
	@InjectMocks
	private UserController userController;
	
	@Test
	public void should_return_ok_logout()  {
		// ARRANGE		
		
		// ACT
		ResponseEntity<Void> response =  userController.logoutUserById(1L);
		// ASSERT
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void should_return_start_of_day()  {
		// ARRANGE
		
		// ACT
		// ASSERT
	}
}
