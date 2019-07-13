package com.demo.restaurant.rest.api.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.demo.restaurant.rest.api.controller.beans.DishRest;
import com.demo.restaurant.rest.api.controller.beans.OrderRest;
import com.demo.restaurant.rest.api.controller.beans.UserRest;
import com.demo.restaurant.rest.api.model.DishCatalog;
import com.demo.restaurant.rest.api.model.Orders;
import com.demo.restaurant.rest.api.model.Users;
import com.demo.restaurant.rest.api.types.OrderState;

@Component
public class MapperOrder {

	public Orders mapToBBDD(OrderRest data, OrderState state) {
		Orders order = new Orders();
		Users user = new Users();
		user.setId(data.getUser().getId());
		order.setUser(user);

		order.setFirstDish(this.mapToBBDD(data.getFirstDish()));
		order.setSecondDish(this.mapToBBDD(data.getSecondDish()));
		order.setDessert(this.mapToBBDD(data.getDessert()));

		order.setId(data.getId());
		order.setDayOrder(data.getDayOrder());
		order.setDayToServe(data.getDayToServe());
		order.setState(state);

		return order;
	}

	public OrderRest mapFromBBDD(Orders data) {
		OrderRest order = new OrderRest();
		DishRest fisrt = new DishRest();
		DishRest second = new DishRest();
		DishRest dessert = new DishRest();
		UserRest user = new UserRest();

		BeanUtils.copyProperties(data, order);
		if (data.getFirstDish() != null) {
			BeanUtils.copyProperties(data.getFirstDish(), fisrt);
		}
		if (data.getSecondDish() != null) {
			BeanUtils.copyProperties(data.getSecondDish(), second);
		}
		if (data.getDessert() != null) {
			BeanUtils.copyProperties(data.getDessert(), dessert);
		}
		BeanUtils.copyProperties(data.getUser(), user, "password", "");
		order.setFirstDish(fisrt);
		order.setSecondDish(second);
		order.setDessert(dessert);
		order.setUser(user);

		return order;
	}

	private DishCatalog mapToBBDD(DishRest dish) {
		DishCatalog joinDish = null;
		if (dish != null && dish.getId() != null) {
			joinDish = new DishCatalog();
			BeanUtils.copyProperties(dish, joinDish);
		}
		return joinDish;
	}

}
