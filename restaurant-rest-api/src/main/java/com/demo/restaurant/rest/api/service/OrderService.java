package com.demo.restaurant.rest.api.service;

import java.util.ArrayList;
import java.util.Calendar;
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
		user.setId(data.getUser().getId());
		order.setUser(user);

		order.setFirstDish(this.mapToBBDDD(data.getFirstDish()));
		order.setSecondDish(this.mapToBBDDD(data.getSecondDish()));
		order.setDessert(this.mapToBBDDD(data.getDessert()));
		
		order.setDayOrder(data.getDayOrder());
		order.setDayToServe(calculateDayToServe(data.getDayOrder()));
		order.setState(OrderState.RECEIVED);

		
		return order;
	}
	

	
	private DishCatalog mapToBBDDD(DishRest dish) {
		DishCatalog joinDish = null;
		if(dish != null && dish.getId() != null) {
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
		if(data.getFirstDish() != null) {
			BeanUtils.copyProperties(data.getFirstDish(), fisrt);
		}
		if(data.getSecondDish() != null) {
			BeanUtils.copyProperties(data.getSecondDish(), second);
		}
		if(data.getDessert() != null) {
			BeanUtils.copyProperties(data.getDessert(), dessert);
		}
		BeanUtils.copyProperties(data.getUser(), user, "password");
		order.setFirstDish(fisrt);
		order.setSecondDish(second);
		order.setDessert(dessert);
		order.setUser(user);

		return order;
	}
	
	


	public List<OrderRest> getAllOrdersByUser(Long id, Date inicialDate, Date endDate) {
		List<OrderRest> returnOrders = new ArrayList<>();
		
		Users user = new Users();
		user.setId(id);
		
		List<Orders> orderByUSer = ordersRepository.findByUserAndDayOrderBetween(user, inicialDate, endDate);
		
		for(Orders temp: orderByUSer) {
			returnOrders.add(this.mapFromBBDD(temp));
		}
		
		
		return returnOrders;
	}

	private Date calculateDayToServe(Date dayOrder) {
		Calendar now = Calendar.getInstance();
		if(dayOrder.after( endOfDay())) {
			return normalizeDate(dayOrder);
		}		
		if(normalizeDate(dayOrder).equals(normalizeDate(now.getTime()))) {
			if(now.get(Calendar.HOUR_OF_DAY)> 11 ) {
				now.add(Calendar.DAY_OF_YEAR, 1);
				return normalizeDate(now.getTime());
			}
		}
		return normalizeDate(now.getTime());
		
	}
	
	private Date normalizeDate(Date date) {
		Calendar cal= Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	private Date endOfDay(){
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 23);
		today.set(Calendar.MINUTE, 59);
		today.set(Calendar.SECOND, 59);
		today.set(Calendar.MILLISECOND, 999);
		return today.getTime();
	}
}
