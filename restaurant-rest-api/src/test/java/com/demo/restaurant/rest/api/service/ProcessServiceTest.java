package com.demo.restaurant.rest.api.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;

import com.demo.restaurant.rest.api.controller.beans.OrderRest;
import com.demo.restaurant.rest.api.controller.beans.UserRest;
import com.demo.restaurant.rest.api.exceptions.IlegalProcessStateException;
import com.demo.restaurant.rest.api.model.Orders;
import com.demo.restaurant.rest.api.model.Session;
import com.demo.restaurant.rest.api.repository.OrdersRepository;
import com.demo.restaurant.rest.api.types.OrderState;
import com.demo.restaurant.rest.api.utils.MapperOrder;

@RunWith(MockitoJUnitRunner.class)
public class ProcessServiceTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	private OrdersRepository ordersRepository;

	@Spy
	private MapperOrder mapperOrder;

	private ProcessService processService;

	@Before
	public void setUp() {
		processService = new ProcessService(ordersRepository, mapperOrder);
	}

	@Test
	public void should_return_exception_if_new_state_not_permit() throws IlegalProcessStateException {
		// ARRANGE
		UserRest user = new UserRest();
		user.setId(2L);
		user.setName("A name");
		user.setPassword("A password");
		user.setEnableSystemOperations(Boolean.FALSE);

		OrderRest order = new OrderRest();
		order.setId(1L);
		order.setDayOrder(new Date());
		order.setDayToServe(new Date());
		order.setState(OrderState.RECEIVED);
		order.setUser(user);
		order.setId(1L);
		order.setId(1L);

		// ASSERT
		thrown.expect(IlegalProcessStateException.class);
		thrown.expectMessage("Transition from state");

		// ACT
		processService.processState(order, OrderState.RECEIVED, user);
		fail();
	}

	@Test
	public void should_return_exception_if_new_state_is_restict_and_user_is_not_enable_system() throws IlegalProcessStateException {
		// ARRANGE
		UserRest user = new UserRest();
		user.setId(2L);
		user.setName("A name");
		user.setPassword("A password");
		user.setEnableSystemOperations(Boolean.FALSE);

		OrderRest order = new OrderRest();
		order.setId(1L);
		order.setDayOrder(new Date());
		order.setDayToServe(new Date());
		order.setState(OrderState.RECEIVED);
		order.setUser(user);
		order.setId(1L);
		order.setId(1L);
		
		// ASSERT
		thrown.expect(IlegalProcessStateException.class);
		thrown.expectMessage(IlegalProcessStateException.ILEGAL_USER_TO_ACTION);

		// ACT
		processService.processState(order, OrderState.DELIVERED, user);
		fail();
	}
	
	@Test
	public void should_return_order_with_new_state() throws IlegalProcessStateException {
		// ARRANGE
		UserRest user = new UserRest();
		user.setId(2L);
		user.setName("A name");
		user.setPassword("A password");
		user.setEnableSystemOperations(Boolean.TRUE);

		OrderRest order = new OrderRest();
		order.setId(1L);
		order.setDayOrder(new Date());
		order.setDayToServe(new Date());
		order.setState(OrderState.RECEIVED);
		order.setUser(user);
		order.setId(1L);
		order.setId(1L);

		when(ordersRepository.save(any(Orders.class))).then(new Answer<Orders>() {
			public Orders answer(InvocationOnMock invocation) throws Throwable {
				return (Orders) invocation.getArguments()[0];
			}

		});
	
		// ACT
		OrderRest newOrder =  processService.processState(order, OrderState.DELIVERED, user);
		
		
		// ASSERT
		assertNotNull(newOrder);
		assertEquals(OrderState.DELIVERED, newOrder.getState());

	}
	
	@Test
	public void should_return_order_with_new_state_not_restict() throws IlegalProcessStateException {
		// ARRANGE
		UserRest user = new UserRest();
		user.setId(2L);
		user.setName("A name");
		user.setPassword("A password");
		user.setEnableSystemOperations(Boolean.TRUE);

		OrderRest order = new OrderRest();
		order.setId(1L);
		order.setDayOrder(new Date());
		order.setDayToServe(new Date());
		order.setState(OrderState.RECEIVED);
		order.setUser(user);
		order.setId(1L);
		order.setId(1L);

		when(ordersRepository.save(any(Orders.class))).then(new Answer<Orders>() {
			public Orders answer(InvocationOnMock invocation) throws Throwable {
				return (Orders) invocation.getArguments()[0];
			}

		});
	
		// ACT
		OrderRest newOrder =  processService.processState(order, OrderState.CANCELED, user);
		
		
		// ASSERT
		assertNotNull(newOrder);
		assertEquals(OrderState.CANCELED, newOrder.getState());

	}
}
