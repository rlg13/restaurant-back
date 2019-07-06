package com.demo.restaurant.rest.api.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.restaurant.rest.api.controller.beans.DishRest;
import com.demo.restaurant.rest.api.controller.beans.OrderRest;
import com.demo.restaurant.rest.api.controller.beans.UserRest;
import com.demo.restaurant.rest.api.exceptions.NoDataFoundException;
import com.demo.restaurant.rest.api.model.DishCatalog;
import com.demo.restaurant.rest.api.model.Orders;
import com.demo.restaurant.rest.api.model.Users;
import com.demo.restaurant.rest.api.repository.OrdersRepository;
import com.demo.restaurant.rest.api.types.OrderState;

import lombok.NonNull;

@Service
public class OrderService {

	@Autowired
	private OrdersRepository ordersRepository;

	public OrderRest getOrder(@NonNull Long id) throws NoDataFoundException {
		OrderRest order;

		try {
			Orders orderData = ordersRepository.findById(id).orElseThrow();
			order = this.mapFromBBDD(orderData);
		} catch (NoSuchElementException e) {
			throw new NoDataFoundException(NoDataFoundException.ORDER_NOT_FOUND);
		}

		return order;
	}

	public OrderRest createOrder(@NonNull OrderRest order) {
		Orders orderData;
		OrderRest newOrder;
		orderData = this.mapToBBDD(order);
		orderData = ordersRepository.save(orderData);
		newOrder = this.mapFromBBDD(orderData);

		return newOrder;
	}

	private Orders mapToBBDD(OrderRest data) {
		Orders order = new Orders();		
		Users user = new Users();
		user.setName(data.getUser().getName());
		order.setUser(user);

		order.setFirstDish(this.mapToBBDDD(data.getFirstDish()));
		order.setSecondDish(this.mapToBBDDD(data.getSecondDish()));
		order.setDessert(this.mapToBBDDD(data.getDessert()));
		
		order.setDayOrder(data.getDayOrder());
		order.setState(OrderState.RECEIVED);

		order.setLastUpdteDate(new Date());
		return order;
	}
	
	private DishCatalog mapToBBDDD(DishRest dish) {
		DishCatalog joinDish = null;
		if(dish != null) {
			joinDish = new DishCatalog();
			BeanUtils.copyProperties(dish, joinDish);
		}
		return joinDish;
	}

	private OrderRest mapFromBBDD(Orders data) {
		OrderRest order = new OrderRest();
		DishRest fisrt = new DishRest();
		DishRest second = new DishRest();
		DishRest dessert = new DishRest();
		UserRest user = new UserRest();

		BeanUtils.copyProperties(data, order);
		BeanUtils.copyProperties(data.getFirstDish(), fisrt);
		BeanUtils.copyProperties(data.getSecondDish(), second);
		BeanUtils.copyProperties(data.getDessert(), dessert);
		BeanUtils.copyProperties(data.getUser(), user, "password");
		order.setFirstDish(fisrt);
		order.setSecondDish(second);
		order.setDessert(dessert);
		order.setUser(user);

		return order;
	}


	public List<OrderRest> getAllOrdersByUser(String name, Date inicialDate, Date endDate) {
		List<OrderRest> returnOrders = new ArrayList<>();
		
		Users user = new Users();
		user.setName(name);
		
		List<Orders> orderByUSer = ordersRepository.findByUserAndDayOrderBetween(user, inicialDate, endDate);
		
		for(Orders temp: orderByUSer) {
			returnOrders.add(this.mapFromBBDD(temp));
		}
		
		
		return returnOrders;
	}

}
