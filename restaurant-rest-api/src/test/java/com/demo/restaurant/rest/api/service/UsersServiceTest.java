package com.demo.restaurant.rest.api.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;

import com.demo.restaurant.rest.api.controller.beans.UserRest;
import com.demo.restaurant.rest.api.exceptions.AlreadyExistsException;
import com.demo.restaurant.rest.api.exceptions.NoDataFoundException;
import com.demo.restaurant.rest.api.model.Users;
import com.demo.restaurant.rest.api.repository.UsersRepository;

@RunWith(MockitoJUnitRunner.class)
public class UsersServiceTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	private UsersRepository usersRepository;

	@InjectMocks
	private UsersService usersService;

	@Test
	public void should_be_return_duplicated_exception() throws AlreadyExistsException {
		// ARRANGE
		UserRest user = new UserRest();
		user.setName("Test User");
		user.setName("A Password");

		when(usersRepository.save(any(Users.class))).thenThrow(DataIntegrityViolationException.class);

		// ASSERT
		thrown.expect(AlreadyExistsException.class);
		thrown.expectMessage(AlreadyExistsException.USER_EXISTS);

		// ACT
		usersService.createUser(user);
		fail();

	}

	@Test
	public void should_be_return_correct_rest_object() throws AlreadyExistsException {
		// ARRANGE
		UserRest user = new UserRest();
		user.setName("Test User");
		user.setPassword("A Password");
		Users userJpaMock = new Users();
		userJpaMock.setId(2L);
		userJpaMock.setName("Test User");
		userJpaMock.setPassword("A Password");
		when(usersRepository.save(any(Users.class))).thenReturn(userJpaMock);
		
		// ACT
		UserRest response = usersService.createUser(user);
		
		// ASSERT
		assertEquals(Long.valueOf(2), response.getId());
		assertEquals(user.getName(), response.getName());
		assertNull(response.getPassword());
	}
	
	@Test
	public void should_return_error_user_not_found() throws NoDataFoundException  {
		// ARRANGE
		UserRest user = new UserRest();
		user.setName("Test User");
		user.setPassword("A Password");
		when(usersRepository.findOneByNameAndPassword(any(String.class),any(String.class))).thenReturn(null);
		
		// ASSERT
		thrown.expect(NoDataFoundException.class);
		thrown.expectMessage(NoDataFoundException.USER_NOT_FOUND);
		
		// ACT
		usersService.getUser(user);
		fail();
	}
	
	@Test
	public void should_return_error_user_without_password() throws NoDataFoundException  {
		// ARRANGE
		UserRest user = new UserRest();
		user.setName("Test User");
		user.setPassword("A Password");
		Users userMockJPA = new Users();
		userMockJPA.setId(2L);
		userMockJPA.setName("Test User");
		userMockJPA.setPassword("A Password");
		
		when(usersRepository.findOneByNameAndPassword(any(String.class),any(String.class))).thenReturn(userMockJPA);

		// ACT
		UserRest userResponse = usersService.getUser(user);
		
		// ASSERT
		assertNotNull(userResponse);
		assertEquals(Long.valueOf(2), userResponse.getId());
		assertEquals(user.getName(), userResponse.getName());
		assertNull( userResponse.getPassword());
		
	}
}
