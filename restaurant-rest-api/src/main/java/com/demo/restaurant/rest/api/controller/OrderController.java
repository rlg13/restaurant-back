package com.demo.restaurant.rest.api.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.demo.restaurant.rest.api.controller.beans.OrderRest;
import com.demo.restaurant.rest.api.controller.beans.UserRest;
import com.demo.restaurant.rest.api.exceptions.IlegalProcessStateException;
import com.demo.restaurant.rest.api.exceptions.InvalidRequestException;
import com.demo.restaurant.rest.api.exceptions.NoDataFoundException;
import com.demo.restaurant.rest.api.service.OrderService;
import com.demo.restaurant.rest.api.service.ProcessService;
import com.demo.restaurant.rest.api.service.SessionService;
import com.demo.restaurant.rest.api.types.OrderState;
import com.demo.restaurant.rest.api.utils.Constants;
import com.demo.restaurant.rest.utils.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private ProcessService processService;

	@Autowired
	private SessionService sessionService;

	@Value("${filter.max-days}")
	private Integer maxDaysFilter;

	@GetMapping(path = "/orders/{id}")
	public ResponseEntity<OrderRest> getOrder(@PathVariable Long id) {

		try {
			OrderRest order = orderService.getOrder(id);

			return ResponseEntity.ok().body(order);
		} catch (NoDataFoundException exp) {
			log.error("Order {} not found", id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NoDataFoundException.ORDER_NOT_FOUND, exp);
		}

	}

	@PostMapping(path = "/orders")
	public ResponseEntity<OrderRest> getOrder(@RequestBody OrderRest newOrder) {

		OrderRest order = orderService.createOrder(newOrder);
		return ResponseEntity.ok().body(order);

	}

	@GetMapping(path = "/orders")
	public ResponseEntity<List<OrderRest>> getAllOrdersByName(
			@RequestParam(name = "initialDate") @DateTimeFormat(iso = ISO.DATE) Date initialDate,
			@RequestParam(name = "endDate") @DateTimeFormat(iso = ISO.DATE) Date endDate,
			@RequestParam(name = "user") Long userId) {
		List<OrderRest> results;

		if (initialDate == null || endDate == null || userId == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, InvalidRequestException.INVALID_PARAMS);
		}
		int days = DateUtils.daysBetweenDates(initialDate, endDate);
		if (days > maxDaysFilter) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, InvalidRequestException.EXCEEDED_MAX_DAYS);
		}
		results = orderService.getAllOrdersByUser(userId, initialDate, endDate);

		return ResponseEntity.ok().body(results);
	}

	@GetMapping(path = "/orders/{id}/process/{newState}")
	public ResponseEntity<OrderRest> processOrder(@RequestHeader(value = Constants.HEADER_SESSION) UUID sessionId,
			@PathVariable Long id, @PathVariable OrderState newState) {
		OrderRest order;

		try {
			order = orderService.getOrder(id);
			UserRest user = sessionService.getUserBySession(sessionId);
			order = processService.processState(order, newState, user);
		} catch (IlegalProcessStateException ilegalState) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ilegalState.getMessage(), ilegalState);
		} catch (NoDataFoundException notFound) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, notFound.getMessage(), notFound);
		}

		return ResponseEntity.ok().body(order);
	}

	@GetMapping(path = "/orders/batch")
	public ResponseEntity<List<OrderRest>> getAllOrdersByStateAndDay(
			@RequestHeader(value = Constants.HEADER_SESSION) String sessionId) {
		List<OrderRest> results;
		try {

			UserRest user = sessionService.getUserBySession(UUID.fromString(sessionId));
			if (!user.getEnableSystemOperations()) {
				throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, Constants.USER_NOT_ALLOWED_OPERATION);
			}
			results = orderService.getAllOrdersByStateAndDayToServe(OrderState.RECEIVED,
					DateUtils.normalizeDate(new Date()));
		} catch (NoDataFoundException notFound) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, notFound.getMessage(), notFound);
		}

		return ResponseEntity.ok().body(results);
	}

}
