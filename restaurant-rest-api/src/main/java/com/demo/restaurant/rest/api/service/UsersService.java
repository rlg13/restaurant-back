package com.demo.restaurant.rest.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.demo.restaurant.rest.api.controller.beans.UserRest;
import com.demo.restaurant.rest.api.exceptions.AlreadyExistsException;
import com.demo.restaurant.rest.api.exceptions.NoDataFoundException;
import com.demo.restaurant.rest.api.model.Users;
import com.demo.restaurant.rest.api.repository.UsersRepository;

import lombok.NonNull;

@Service
public class UsersService {

	@Autowired
	private UsersRepository usersRepository;
	
//	@Autowired SessionRepository sessionRepository;
/*
	public List<UserRest> getAllUsers() {

		List<UserRest> allUser = new ArrayList<>();

		for (Users temp : usersRepository.findAll()) {
			UserRest dest = new UserRest();
			BeanUtils.copyProperties(temp, dest);
			allUser.add(dest);
		}

		return allUser;
	}*/

	public UserRest createUser(UserRest user) throws AlreadyExistsException {
		Users newUser = new Users();		
		try {
			BeanUtils.copyProperties(user, newUser);
			newUser = usersRepository.save(newUser);
			BeanUtils.copyProperties(newUser, user);
			return user;
		} catch (DataIntegrityViolationException exp) { // User name exists
			throw new AlreadyExistsException(AlreadyExistsException.USER_EXISTS);
		}

	}

/*	public UserRest getUser(@NonNull String name) throws NoDataFoundException {
		UserRest user = new UserRest();
		try {
			Users userData = usersRepository.findOneByName(name);
			BeanUtils.copyProperties(userData, user, "password");
		} catch (NoSuchElementException e) {
			throw new NoDataFoundException(NoDataFoundException.USER_NOT_FOUND);
		}

		return user;
	}*/

	public UserRest getUser(@NonNull UserRest user) throws NoDataFoundException {
		UserRest userLoged = new UserRest();

		Users userData = usersRepository.findOneByNameAndPassword(user.getName(),user.getPassword());
		if(userData == null) {
			throw new NoDataFoundException(NoDataFoundException.USER_NOT_FOUND);
		}
		BeanUtils.copyProperties(userData, userLoged,"password");

		return userLoged;
	}

}
