package com.demo.restaurant.rest.api.service;

import java.util.Calendar;

import org.springframework.stereotype.Service;

import com.demo.restaurant.rest.api.controller.beans.OrderRest;
import com.demo.restaurant.rest.api.controller.beans.UserRest;
import com.demo.restaurant.rest.api.exceptions.IlegalProcessStateException;
import com.demo.restaurant.rest.api.repository.OrdersRepository;
import com.demo.restaurant.rest.api.types.OrderState;
import com.demo.restaurant.rest.api.utils.MapperOrder;
import com.demo.restaurant.rest.utils.DateUtils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ProcessService {

	private OrdersRepository ordersRepository;

	private MapperOrder mapperOrder;

	public OrderRest processState(OrderRest order, OrderState newState, UserRest user)
			throws IlegalProcessStateException {
		OrderState actualState = order.getState();

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

		return mapperOrder.mapFromBBDD(ordersRepository.save(mapperOrder.mapToBBDD(order, order.getState())));
	}

	private void checkNewStateFromReceived(OrderRest order, OrderState newState, UserRest user)
			throws IlegalProcessStateException {

		if (!OrderState.CANCELED.equals(newState) && !OrderState.DELIVERED.equals(newState)) {
			throw new IlegalProcessStateException(OrderState.RECEIVED, newState);
		}
		if (DateUtils.normalizeDate(order.getDayToServe()).before(
				DateUtils.normalizeDate(Calendar.getInstance().getTime())) && OrderState.CANCELED.equals(newState)) {
			throw new IlegalProcessStateException(IlegalProcessStateException.ILEGAL_CANCELATION);
		}
		if (OrderState.DELIVERED.equals(newState) && !user.getEnableSystemOperations()) {
			throw new IlegalProcessStateException(IlegalProcessStateException.ILEGAL_USER_TO_ACTION);
		}
		if (OrderState.CANCELED.equals(newState) && !order.getUser().getId().equals(user.getId())) {
			throw new IlegalProcessStateException(IlegalProcessStateException.ILEGAL_USER_TO_ACTION);
		}
	}

	private void checkNewStateFromDelivered(OrderRest order, OrderState newState, UserRest user)
			throws IlegalProcessStateException {

		if (!OrderState.PAID.equals(newState)) {
			throw new IlegalProcessStateException(OrderState.DELIVERED, newState);
		}
		if (!order.getUser().getId().equals(user.getId())) {
			throw new IlegalProcessStateException(IlegalProcessStateException.ILEGAL_USER_TO_ACTION);
		}
	}

}
