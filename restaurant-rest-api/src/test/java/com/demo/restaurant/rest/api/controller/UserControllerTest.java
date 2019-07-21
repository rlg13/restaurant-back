package com.demo.restaurant.rest.api.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.demo.restaurant.rest.api.controller.beans.UserRest;
import com.demo.restaurant.rest.api.exceptions.AlreadyExistsException;
import com.demo.restaurant.rest.api.model.Session;
import com.demo.restaurant.rest.api.model.Users;
import com.demo.restaurant.rest.api.service.SessionService;
import com.demo.restaurant.rest.api.service.UsersService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
@ActiveProfiles(profiles = { "test" })
public class UserControllerTest {

	private UUID mockUUID;

	private Users mockUser;

	private Session mockSession;

	ObjectWriter ow;

	@Autowired
	private MockMvc mvc;

	@MockBean
	private UsersService usersService;

	@MockBean
	private SessionService sessionService;

	@Before
	public void setup() {
		mockUUID = UUID.randomUUID();
		mockUser = new Users();
		mockUser.setName("A name");

		mockSession = new Session();
		mockSession.setId(mockUUID);
		mockSession.setUser(mockUser);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ow = mapper.writer().withDefaultPrettyPrinter();

	}

	@Test
	public void should_return_ok_logout() throws Exception {

		// ARRANGE
		doNothing().when(sessionService).invalidateSession(any(Long.class));

		// ACT & ASSERT
		mvc.perform(delete("/users/logout").param("id", "1")).andExpect(status().isOk());

	}

	@Test
	public void should_return_new_session_user()
			throws UnsupportedEncodingException, JsonProcessingException, Exception {
		// ARRANGE
		doAnswer(new Answer<UserRest>() {
			public UserRest answer(InvocationOnMock invocation) throws Throwable {
				UserRest user = (UserRest) invocation.getArguments()[0];
				return user;
			}
		}).when(usersService).getUser(any(UserRest.class));

		doReturn(mockSession).when(sessionService).createSession(any(UserRest.class));

		UserRest userCall = new UserRest();
		userCall.setName("A user");
		// ACT & ASSERT
		String content = ow.writeValueAsString(userCall);
		mvc.perform(post("/users/login").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
				.andExpect(status().isOk()).andExpect(content().string(containsString(mockUUID.toString())));

	}

	@Test
	public void should_return_exception_when_create_an_existent_user()
			throws UnsupportedEncodingException, JsonProcessingException, Exception {
		// ARRANGE
		doThrow(AlreadyExistsException.class).when(usersService).createUser(any(UserRest.class));

		UserRest userCall = new UserRest();
		userCall.setName("A user");
		userCall.setPassword("A Password");
		String content = ow.writeValueAsString(userCall);
		// ACT & ASSERT
		mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
				.andExpect(status().isConflict())
				.andExpect(status().reason(containsString(AlreadyExistsException.USER_EXISTS)));

	}

}
