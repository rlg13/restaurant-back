package com.demo.restaurant.rest.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.demo.restaurant.rest.api.controller.beans.DishRest;
import com.demo.restaurant.rest.api.exceptions.AlreadyExistsException;
import com.demo.restaurant.rest.api.exceptions.NoDataFoundException;
import com.demo.restaurant.rest.api.service.DishService;
import com.demo.restaurant.rest.api.types.DishType;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
@RestController
public class DishController {

	private DishService dishService;

	@GetMapping(path = "/dishes/{id}")
	public ResponseEntity<DishRest> getDish(@PathVariable Long id) {

		try {
			DishRest dish = dishService.getDish(id);
			return ResponseEntity.ok().body(dish);
		} catch (NoDataFoundException exp) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NoDataFoundException.DISH_NOT_FOUND, exp);
		}

	}

	@GetMapping(path = "/dishes/type/{type}")
	public ResponseEntity<List<DishRest>> getDishesByType(@PathVariable DishType type) {
		log.debug("Dish service called whith parameter: " + type.name());

		return ResponseEntity.ok().body(dishService.getDishesByType(type));
	}

	@PostMapping(path = "/dishes")
	public ResponseEntity<DishRest> createDish(@RequestBody DishRest newDish) {

		try {
			return ResponseEntity.ok().body(dishService.createDish(newDish));
		} catch (AlreadyExistsException exp) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, AlreadyExistsException.DISH_EXISTS, exp);
		}
	}
}
