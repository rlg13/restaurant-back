package com.demo.restaurant.rest.api.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.demo.restaurant.rest.api.controller.beans.DishRest;
import com.demo.restaurant.rest.api.controller.beans.OrderRest;
import com.demo.restaurant.rest.api.controller.beans.UserRest;
import com.demo.restaurant.rest.api.exceptions.NoDataFoundException;
import com.demo.restaurant.rest.api.service.OrderService;
import com.demo.restaurant.rest.api.service.ProcessService;
import com.demo.restaurant.rest.api.service.SessionService;
import com.demo.restaurant.rest.api.types.OrderState;
import com.demo.restaurant.rest.api.utils.Constants;
import com.demo.restaurant.rest.api.utils.ProjectVars;

@RunWith(SpringRunner.class)
@WebMvcTest(value = OrderController.class)
@ActiveProfiles(profiles = { "test" })
public class OrderControllerTest {

	private List<OrderRest> orderArayMock;

	private UserRest systemUser;

	@Autowired
	private MockMvc mvc;

	@MockBean
	private OrderService orderService;

	@MockBean
	private ProcessService processService;

	@MockBean
	private SessionService sessionService;

	@MockBean
	private ProjectVars projectVars;

	@Before
	public void setup() {

		orderArayMock = new ArrayList<>();

		systemUser = new UserRest();

		systemUser.setId(1L);
		systemUser.setName("System");
		systemUser.setEnableSystemOperations(true);

		OrderRest orderMock = new OrderRest();
		orderMock.setId(1L);
		orderMock.setDayOrder(new Date());
		orderMock.setDayToServe(new Date());
		orderMock.setState(OrderState.RECEIVED);

		orderMock.setUser(new UserRest());
		orderMock.getUser().setId(2L);
		orderMock.getUser().setName("A name");

		orderMock.setFirstDish(new DishRest());
		orderMock.getFirstDish().setId(1L);
		orderMock.getFirstDish().setName("Salad");

		orderMock.setSecondDish(new DishRest());
		orderMock.getSecondDish().setId(2L);
		orderMock.getSecondDish().setName("Fish");

		orderMock.setDessert(new DishRest());
		orderMock.getDessert().setId(3L);
		orderMock.getDessert().setName("Fruit");

		orderArayMock.add(orderMock);
	}

	@Test
	public void should_return_one_order() throws Exception {

		// ARRANGE
		doReturn(systemUser).when(sessionService).getUserBySession(any(UUID.class));
		doReturn(orderArayMock).when(orderService).getAllOrdersByStateAndDayToServe(any(OrderState.class),
				any(Date.class));

		// ACT & ASSERT
		mvc.perform(get("/orders/batch").header(Constants.HEADER_SESSION, UUID.randomUUID().toString()))
				.andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)));

	}

	@Test
	public void should_return_method_not_allowed() throws Exception {

		// ARRANGE
		systemUser.setEnableSystemOperations(false);
		doReturn(systemUser).when(sessionService).getUserBySession(any(UUID.class));
		doReturn(orderArayMock).when(orderService).getAllOrdersByStateAndDayToServe(any(OrderState.class),
				any(Date.class));

		// ACT & ASSERT
		mvc.perform(get("/orders/batch").header(Constants.HEADER_SESSION, UUID.randomUUID().toString()))
				.andExpect(status().isMethodNotAllowed())
				.andExpect(status().reason(containsString(Constants.USER_NOT_ALLOWED_OPERATION)));
	}

	@Test
	public void should_return_exception_exists_dish() throws Exception {

		// ARRANGE
		doThrow(NoDataFoundException.class).when(sessionService).getUserBySession(any(UUID.class));

		// ACT & ASSERT
		mvc.perform(get("/orders/batch").header(Constants.HEADER_SESSION, UUID.randomUUID().toString()))
				.andExpect(status().isNotFound())
				.andExpect(status().reason(containsString(NoDataFoundException.SESSION_NOT_FOUND)));

	}

}
