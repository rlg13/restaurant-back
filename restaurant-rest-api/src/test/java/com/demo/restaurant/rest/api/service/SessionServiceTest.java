package com.demo.restaurant.rest.api.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.demo.restaurant.rest.api.controller.beans.UserRest;
import com.demo.restaurant.rest.api.exceptions.NoDataFoundException;
import com.demo.restaurant.rest.api.model.Session;
import com.demo.restaurant.rest.api.model.Users;
import com.demo.restaurant.rest.api.repository.SessionRepository;

@RunWith(MockitoJUnitRunner.class)
public class SessionServiceTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	SessionRepository sessionRepository;

	@InjectMocks
	private SessionService sessionService;

	@Test
	public void should_return_session_with_date_to_future() {
		// ARRANGE
		UserRest user = new UserRest();
		user.setName("Test User");
		user.setName("A Password");

		when(sessionRepository.save(any(Session.class))).then(new Answer<Session>() {
			public Session answer(InvocationOnMock invocation) throws Throwable {
				return (Session) invocation.getArguments()[0];
			}

		});
		// ACT
		Session sessionReturn = sessionService.createSession(user);
		// ASSERT
		assertNotNull(sessionReturn);
		assertNotNull(sessionReturn.getExpirationDate());
		assertTrue(sessionReturn.getExpirationDate().after(new Date()));
	}

	@Test
	public void should_return_exception_if_session_not_exists() throws NoDataFoundException {
		// ARRANGE
		UUID uuid = UUID.randomUUID();		

		// ASSERT
		thrown.expect(NoDataFoundException.class);
		thrown.expectMessage(NoDataFoundException.SESSION_NOT_FOUND);

		// ACT
		sessionService.refreshSession(uuid);
		fail();
	}

	@Test
	public void should_return_exception_if_suser_not_have_session_stablish() throws NoDataFoundException {
		// ARRANGE
		UUID uuid = UUID.randomUUID();
		when(sessionRepository.findById(any(UUID.class))).thenThrow(NoSuchElementException.class);

		// ASSERT
		thrown.expect(NoDataFoundException.class);
		thrown.expectMessage(NoDataFoundException.SESSION_NOT_FOUND);

		// ACT
		sessionService.getUserBySession(uuid);
		fail();
	}
	
	@Test
	public void should_return_user_if_session_is_valid() throws NoDataFoundException {
		// ARRANGE
		UUID uuid = UUID.randomUUID();
		Users userMock = new Users();
		userMock.setId(2L);
		userMock.setName("A name");
		userMock.setPassword("A password");
		Session sessionMock = new Session();
		sessionMock.setId(uuid);
		sessionMock.setUser(userMock);
		Optional<Session> sessionOptional = Optional.of(sessionMock);
		
		when(sessionRepository.findById(any(UUID.class))).thenReturn(sessionOptional);

		// ACT
		UserRest user = sessionService.getUserBySession(uuid);		
		
		// ASSERT
		assertNotNull(user);
		//assertNull(user.getPassword());
		assertEquals(userMock.getName(), user.getName());
	}
}
