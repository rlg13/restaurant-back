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
import com.demo.restaurant.rest.api.utils.MapperOrder;
import com.demo.restaurant.rest.utils.DateUtils;

import lombok.NonNull;

@Service
public class OrderService {

	@Autowired
	private OrdersRepository ordersRepository;
	
	@Autowired
	private MapperOrder mapperOrder;

	public OrderRest getOrder(@NonNull Long id) throws NoDataFoundException {
		OrderRest order;

		try {
			Orders orderData = ordersRepository.findById(id).orElseThrow();
			order = mapperOrder.mapFromBBDD(orderData);
		} catch (NoSuchElementException e) {
			throw new NoDataFoundException(NoDataFoundException.ORDER_NOT_FOUND);
		}

		return order;
	}

	public OrderRest createOrder(@NonNull OrderRest order) {
		Orders orderData;
		OrderRest newOrder;
		orderData = mapperOrder.mapCreateToBBDD(order);
		orderData = ordersRepository.save(orderData);
		newOrder = mapperOrder.mapFromBBDD(orderData);

		return newOrder;
	}


	


	public List<OrderRest> getAllOrdersByUser(Long id, Date inicialDate, Date endDate) {
		List<OrderRest> returnOrders = new ArrayList<>();
		
		Users user = new Users();
		user.setId(id);
		
		List<Orders> orderByUSer = ordersRepository.findByUserAndDayOrderBetween(user, inicialDate, endDate);
		
		for(Orders temp: orderByUSer) {
			returnOrders.add(mapperOrder.mapFromBBDD(temp));
		}
		
		
		return returnOrders;
	}
	public List<OrderRest> getAllOrdersByStateAndDayToServe(OrderState state, Date dayToServe) {
		List<OrderRest> returnOrders = new ArrayList<>();
		
		List<Orders> orderByUSer = ordersRepository.findByStateAndDayToServe(state, dayToServe);
		
		for(Orders temp: orderByUSer) {
			returnOrders.add(mapperOrder.mapFromBBDD(temp));
		}
		
		
		return returnOrders;
	}


}
