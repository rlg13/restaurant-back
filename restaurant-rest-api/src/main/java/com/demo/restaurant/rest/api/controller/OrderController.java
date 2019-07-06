package com.demo.restaurant.rest.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.demo.restaurant.rest.api.controller.beans.FilterParams;
import com.demo.restaurant.rest.api.controller.beans.OrderRest;
import com.demo.restaurant.rest.api.exceptions.InvalidRequestException;
import com.demo.restaurant.rest.api.exceptions.NoDataFoundException;
import com.demo.restaurant.rest.api.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin("*")
@RestController
public class OrderController {

	@Autowired
	private OrderService orderService;

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
	public ResponseEntity<List<OrderRest>> getAllOrdersByName(@RequestBody FilterParams filterParams) {
		List<OrderRest> results;
		if (filterParams.getInicialDate() == null || filterParams.getEndDate() == null
				|| filterParams.getUser() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, InvalidRequestException.INVALID_PARAMS);
		}
		// TODO: Filtrar con excepcion mas de X dias ??

		results = orderService.getAllOrdersByUser(filterParams.getUser().getName(), filterParams.getInicialDate(),
				filterParams.getEndDate());

		return ResponseEntity.ok().body(results);

	}

}
