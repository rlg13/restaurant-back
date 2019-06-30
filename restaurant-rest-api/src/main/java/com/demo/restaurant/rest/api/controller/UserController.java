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

import com.demo.restaurant.rest.api.controller.beans.UserRest;
import com.demo.restaurant.rest.api.exceptions.AlreadyExistsException;
import com.demo.restaurant.rest.api.exceptions.NoDataFoundException;
import com.demo.restaurant.rest.api.service.UsersService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin("*")
@RestController
public class UserController {
	
	@Autowired
	private UsersService usersService;

	@GetMapping(path = "/users")
	public ResponseEntity<List<UserRest>> getAllUsers() {

		return ResponseEntity.ok().body(usersService.getAllUsers());

	}
	
	@GetMapping(path = "/users/{id}")
	public ResponseEntity<UserRest> getUser(@PathVariable Long id) {

		try {
			UserRest user =usersService.getUser(id);
			return ResponseEntity.ok().body(user);
		} catch (NoDataFoundException exp) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NoDataFoundException.USER_NOT_FOUND,exp);
		}
	}
	
	@PostMapping(path = "/users/login")
	public ResponseEntity<UserRest> getUserByName(@RequestBody UserRest user) {
		try {		
			return ResponseEntity.ok().body(usersService.getUser(user));
		} catch (NoDataFoundException exp) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NoDataFoundException.USER_NOT_FOUND,exp);
		}		
	}


	@PostMapping(path = "/users")
	public ResponseEntity<UserRest> createUser(@RequestBody UserRest newUser) {
		log.debug("Create User called");
		try {
			return ResponseEntity.ok().body(usersService.createUser(newUser));
		} catch (AlreadyExistsException exp) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, AlreadyExistsException.USER_EXISTS,exp);
		}
	}
	
	

	

//	@GetMapping(path = "/load")
//	public ResponseEntity<Void> movida() {
//		Users usuario = new Users();
//		usuario.setName("movidas");
//		Orders order = new Orders();
//		order.setDayOrder(new Date());
//		order.setState(OrderState.RECEIVED);
//		order.setLastUpdteDate(new Date());
//		order.setUser(usuario);
//		usersRepository.save(usuario);
//		ordersRepository.save(order);
//		return ResponseEntity.ok().body(null);
//	}

}
