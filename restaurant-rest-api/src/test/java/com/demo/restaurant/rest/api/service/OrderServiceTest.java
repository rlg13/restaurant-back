package com.demo.restaurant.rest.api.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

import com.demo.restaurant.rest.api.controller.beans.DishRest;
import com.demo.restaurant.rest.api.controller.beans.OrderRest;
import com.demo.restaurant.rest.api.controller.beans.UserRest;
import com.demo.restaurant.rest.api.exceptions.IlegalProcessStateException;
import com.demo.restaurant.rest.api.exceptions.NoDataFoundException;
import com.demo.restaurant.rest.api.model.DishCatalog;
import com.demo.restaurant.rest.api.model.Orders;
import com.demo.restaurant.rest.api.model.Users;
import com.demo.restaurant.rest.api.repository.OrdersRepository;
import com.demo.restaurant.rest.api.types.DishType;
import com.demo.restaurant.rest.api.types.OrderState;
import com.demo.restaurant.rest.api.utils.MapperOrder;
import com.demo.restaurant.rest.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	private OrdersRepository ordersRepository;

	@Spy
	private MapperOrder mapperOrder;

	private OrderService orderService;

	@Before
	public void setUp() {
		orderService = new OrderService(ordersRepository, mapperOrder);
	}

	@Test
	public void should_return_exception_if_element_not_exists() throws NoDataFoundException {
		// ARRANGE
		Long id = Long.valueOf(1L);

		when(ordersRepository.findById(any(Long.class))).thenThrow(NoSuchElementException.class);
		// ASSERT
		thrown.expect(NoDataFoundException.class);
		thrown.expectMessage(NoDataFoundException.ORDER_NOT_FOUND);

		// ACT
		orderService.getOrder(id);
		fail();
	}

	@Test
	public void should_return_order() throws NoDataFoundException {
		// ARRANGE
		Long id = Long.valueOf(1L);

		DishCatalog dish = new DishCatalog();
		dish.setId(1L);
		dish.setName("Salad");
		dish.setType(DishType.FIRST);

		Users user = new Users();
		user.setId(2L);
		user.setName("A name");

		Orders order = new Orders();
		order.setId(1L);
		order.setDayOrder(new Date());
		order.setDayToServe(new Date());
		order.setState(OrderState.RECEIVED);
		order.setUser(user);
		order.setFirstDish(dish);
		Optional<Orders> orderOptional = Optional.of(order);

		when(ordersRepository.findById(any(Long.class))).thenReturn(orderOptional);

		// ACT
		OrderRest orderRest = orderService.getOrder(id);

		// ASSERT
		assertNotNull(orderRest);
		assertNotNull(orderRest.getUser());
		assertNotNull(orderRest.getFirstDish());
		assertNull(orderRest.getSecondDish());
		assertEquals(id, orderRest.getId());
		assertEquals(user.getName(), orderRest.getUser().getName());
		assertEquals(order.getFirstDish().getId(), orderRest.getFirstDish().getId());
		assertEquals(order.getDayToServe(), orderRest.getDayToServe());

	}

	@Test
	public void should_return_new_order() throws NoDataFoundException {
		// ARRANGE
		DishRest dish = new DishRest();
		dish.setId(1L);
		dish.setName("Salad");
		dish.setType(DishType.FIRST);

		UserRest user = new UserRest();
		user.setName("A name");

		OrderRest order = new OrderRest();
		order.setDayOrder(new Date());
		
		order.setState(OrderState.RECEIVED);
		order.setUser(user);
		order.setFirstDish(dish);

		when(ordersRepository.save(any(Orders.class))).then(new Answer<Orders>() {
			public Orders answer(InvocationOnMock invocation) throws Throwable {
				Orders orderMock = (Orders) invocation.getArguments()[0];
				orderMock.setId(2L);
				return orderMock;
			}
		});

		// ACT
		OrderRest orderRest = orderService.createOrder(order);

		// ASSERT
		assertNotNull(orderRest);
		assertNotNull(orderRest.getUser());
		assertNotNull(orderRest.getFirstDish());
		assertNull(orderRest.getSecondDish());
		assertEquals(Long.valueOf(2l), orderRest.getId());
		assertEquals(user.getId(), orderRest.getUser().getId());
		assertEquals(order.getFirstDish().getId(), orderRest.getFirstDish().getId());
		assertEquals(DateUtils.calculateDayToServe(order.getDayOrder()), orderRest.getDayToServe());

	}

	@Test
	public void should_return_list_orders_of_user() throws IlegalProcessStateException {
		// ARRANGE
		DishCatalog dish = new DishCatalog();
		dish.setId(1L);
		dish.setName("Salad");
		dish.setType(DishType.FIRST);

		Users user = new Users();
		user.setId(2L);
		user.setName("A name");

		Orders order = new Orders();
		order.setId(2L);
		order.setDayOrder(new Date());
		order.setDayToServe(new Date());
		order.setState(OrderState.RECEIVED);
		order.setUser(user);
		order.setFirstDish(dish);

		List<Orders> returnList = new ArrayList<>();
		returnList.add(order);

		when(ordersRepository.findByUserAndDayOrderBetween(any(Users.class), any(Date.class), any(Date.class)))
				.thenReturn(returnList);

		// ACT
		List<OrderRest> orderList = orderService.getAllOrdersByUser(user.getId(), new Date(), new Date());

		// ASSERT
		assertEquals(1, orderList.size());
		OrderRest orderRest = orderList.get(0);
		assertNotNull(orderRest);
		assertNotNull(orderRest.getUser());
		assertNotNull(orderRest.getFirstDish());
		assertNull(orderRest.getSecondDish());
		assertEquals(Long.valueOf(2L), orderRest.getId());
		assertEquals(user.getName(), orderRest.getUser().getName());
		assertEquals(order.getFirstDish().getId(), orderRest.getFirstDish().getId());
		assertEquals(order.getDayToServe(), orderRest.getDayToServe());

	}

	@Test
	public void should_return_list_orders_of_user_and_state() throws IlegalProcessStateException {
		// ARRANGE
		DishCatalog dish = new DishCatalog();
		dish.setId(1L);
		dish.setName("Salad");
		dish.setType(DishType.FIRST);

		Users user = new Users();
		user.setId(2L);
		user.setName("A name");

		Orders order = new Orders();
		order.setId(2L);
		order.setDayOrder(new Date());
		order.setDayToServe(new Date());
		order.setState(OrderState.RECEIVED);
		order.setUser(user);
		order.setFirstDish(dish);

		List<Orders> returnList = new ArrayList<>();
		returnList.add(order);

		when(ordersRepository.findByStateAndDayToServe(any(OrderState.class), any(Date.class))).thenReturn(returnList);

		// ACT
		List<OrderRest> orderList = orderService.getAllOrdersByStateAndDayToServe(OrderState.RECEIVED, new Date());

		// ASSERT
		assertEquals(1, orderList.size());
		OrderRest orderRest = orderList.get(0);
		assertNotNull(orderRest);
		assertNotNull(orderRest.getUser());
		assertNotNull(orderRest.getFirstDish());
		assertNull(orderRest.getSecondDish());
		assertEquals(Long.valueOf(2L), orderRest.getId());
		assertEquals(user.getName(), orderRest.getUser().getName());
		assertEquals(order.getFirstDish().getId(), orderRest.getFirstDish().getId());
		assertEquals(order.getDayToServe(), orderRest.getDayToServe());

	}
}
