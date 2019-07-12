package com.demo.restaurant.rest.api.service;

import java.util.Calendar;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.restaurant.rest.api.controller.beans.OrderRest;
import com.demo.restaurant.rest.api.controller.beans.UserRest;
import com.demo.restaurant.rest.api.exceptions.IlegalProcessStateException;
import com.demo.restaurant.rest.api.model.Orders;
import com.demo.restaurant.rest.api.model.Users;
import com.demo.restaurant.rest.api.repository.OrdersRepository;
import com.demo.restaurant.rest.api.types.OrderState;
import com.demo.restaurant.rest.api.utils.MapperOrder;
import com.demo.restaurant.rest.utils.DateUtils;

@Service
public class ProcessService {

	@Autowired
	private OrdersRepository ordersRepository;
	
	@Autowired
	private MapperOrder mapperOrder;

	public OrderRest processState(OrderRest order, OrderState newState, UserRest user) throws IlegalProcessStateException {
		OrderState actualState = order.getState();
		Orders orderBBDD;
		
		switch (actualState) {
		case RECEIVED:
			checkNewStateFromReceived(order, newState, user);
			order.setState(newState);
			break;

		case DELIVERED:
			checkNewStateFromDelivered(order, newState, user);
			order.setState(newState);
			break;
		default:
			throw new IlegalProcessStateException(actualState, newState);
		}

		orderBBDD = mapperOrder.mapUpdateToBBDD(order);
		return mapperOrder.mapFromBBDD(ordersRepository.save(orderBBDD));
	}

	private void checkNewStateFromReceived(OrderRest order, OrderState newState, UserRest user)
			throws IlegalProcessStateException {
		if (newState != OrderState.CANCELED && newState != OrderState.DELIVERED) {
			throw new IlegalProcessStateException(OrderState.RECEIVED, newState);
		}
		if (DateUtils.normalizeDate(order.getDayToServe())
				.before(DateUtils.normalizeDate(Calendar.getInstance().getTime())) && newState == OrderState.CANCELED) {
			throw new IlegalProcessStateException(IlegalProcessStateException.ILEGAL_CANCELATION);
		}
		if (newState == OrderState.DELIVERED && !user.getEnableSystemOperations()) {
			throw new IlegalProcessStateException(IlegalProcessStateException.ILEGAL_USER_TO_ACTION);
		}
		if (newState == OrderState.CANCELED && !order.getUser().getId().equals(user.getId())) {
			throw new IlegalProcessStateException(IlegalProcessStateException.ILEGAL_USER_TO_ACTION);
		}
	}

	private void checkNewStateFromDelivered(OrderRest order, OrderState newState, UserRest user)
			throws IlegalProcessStateException {
		if (newState != OrderState.PAID) {
			throw new IlegalProcessStateException(OrderState.DELIVERED, newState);
		}
		if (!order.getUser().getId().equals(user.getId())) {
			throw new IlegalProcessStateException(IlegalProcessStateException.ILEGAL_USER_TO_ACTION);
		}
	}

}
