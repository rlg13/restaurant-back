package com.demo.restaurant.rest.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.demo.restaurant.rest.api.controller.beans.UserRest;
import com.demo.restaurant.rest.api.exceptions.AlreadyExistsException;
import com.demo.restaurant.rest.api.exceptions.NoDataFoundException;
import com.demo.restaurant.rest.api.model.Session;
import com.demo.restaurant.rest.api.service.SessionService;
import com.demo.restaurant.rest.api.service.UsersService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
public class UserController {
	
	@Autowired
	private UsersService usersService;
	
	@Autowired 
	SessionService sessionService;
/*
	@GetMapping(path = "/users/{id}")
	public ResponseEntity<UserRest> getUser(@PathVariable Long id) {

		try {
			UserRest user =usersService.getUser(id);
			return ResponseEntity.ok().body(user);
		} catch (NoDataFoundException exp) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NoDataFoundException.USER_NOT_FOUND,exp);
		}
	}*/
	
	@DeleteMapping(path = "/users/login/{id}")
	public ResponseEntity<UserRest> getUserByName(@PathVariable Long id) {
		sessionService.invalidateSession(id);		
		return ResponseEntity.ok().build();
	}
	@PostMapping(path = "/users/login")
	public ResponseEntity<UserRest> getUserByName(@RequestBody UserRest user) {
		try {
			UserRest userLogged = usersService.getUser(user);
			Session session = sessionService.createSession(userLogged);
			userLogged.setSessionId(session.getId().toString());
			return ResponseEntity.ok().body(userLogged);
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
}
