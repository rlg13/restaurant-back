package com.demo.restaurant.rest.api.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.demo.restaurant.rest.api.controller.beans.DishRest;
import com.demo.restaurant.rest.api.controller.beans.OrderRest;
import com.demo.restaurant.rest.api.controller.beans.UserRest;
import com.demo.restaurant.rest.api.model.DishCatalog;
import com.demo.restaurant.rest.api.model.Orders;
import com.demo.restaurant.rest.api.model.Users;
import com.demo.restaurant.rest.api.types.DishType;
import com.demo.restaurant.rest.api.types.OrderState;

@RunWith(MockitoJUnitRunner.class)
public class MapperOrderTest {

	@InjectMocks
	private MapperOrder mapperOrder;

	@Test
	public void shoul_be_map_order_entity() {
		// ARRANGE
		DishRest dishTest = new DishRest();
		dishTest.setId(2L);
		dishTest.setName("Pineaple");
		dishTest.setType(DishType.FIRST);

		UserRest userTest = new UserRest();
		userTest.setId(1L);
		userTest.setName("Test");

		OrderRest orderTest = new OrderRest();
		orderTest.setDayOrder(new Date());
		orderTest.setDayToServe(new Date());
		orderTest.setFirstDish(dishTest);
		orderTest.setState(OrderState.DELIVERED);
		orderTest.setFirstDish(dishTest);
		orderTest.setUser(userTest);

		// ACT
		Orders orderJPA = mapperOrder.mapToBBDD(orderTest, OrderState.RECEIVED);

		// ASSERT
		assertNotNull(orderJPA);
		assertNotNull(orderJPA.getUser());
		assertEquals(Long.valueOf(1), orderJPA.getUser().getId());
		assertNull(orderJPA.getUser().getName());

		assertNotNull(orderJPA.getFirstDish());
		assertNotNull(orderJPA.getFirstDish().getName());
		assertNull(orderJPA.getSecondDish());

		assertEquals(OrderState.RECEIVED, orderJPA.getState());

	}

	@Test
	public void should_be_map_order_from_bbdd() throws ParseException {
		// ARRANGE
		DishCatalog dishTest = new DishCatalog();
		dishTest.setId(2L);
		dishTest.setName("Pineaple");
		dishTest.setType(DishType.FIRST);

		Users userTest = new Users();
		userTest.setId(1L);
		userTest.setName("Test");
		userTest.setPassword("A Password");

		Orders orderTest = new Orders();

		orderTest.setUser(userTest);
		orderTest.setFirstDish(dishTest);
		orderTest.setDayToServe(new Date());
		orderTest.setState(OrderState.RECEIVED);
		// ACT
		OrderRest orderRest = mapperOrder.mapFromBBDD(orderTest);

		// ASSERT
		assertNotNull(orderRest);
		assertNotNull(orderRest.getUser());
		assertEquals(Long.valueOf(1), orderTest.getUser().getId());
		assertNotNull(orderRest.getUser().getName());
		assertNull(orderRest.getUser().getPassword());

		assertNotNull(orderRest.getFirstDish());
		assertNotNull(orderRest.getFirstDish().getName());
		assertNull(orderRest.getSecondDish());
		assertEquals(OrderState.RECEIVED, orderRest.getState());
	}
}
