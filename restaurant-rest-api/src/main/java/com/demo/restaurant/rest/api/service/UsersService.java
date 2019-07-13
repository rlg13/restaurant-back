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

	public UserRest createUser(UserRest user) throws AlreadyExistsException {
		Users newUser = new Users();

		try {
			BeanUtils.copyProperties(user, newUser);
			newUser = usersRepository.save(newUser);
			BeanUtils.copyProperties(newUser, user);
			return user;
		} catch (DataIntegrityViolationException exp) {
			throw new AlreadyExistsException(AlreadyExistsException.USER_EXISTS);
		}

	}

	public UserRest getUser(@NonNull UserRest user) throws NoDataFoundException {
		UserRest userLoged = new UserRest();

		Users userData = usersRepository.findOneByNameAndPassword(user.getName(), user.getPassword());
		if (userData == null) {
			throw new NoDataFoundException(NoDataFoundException.USER_NOT_FOUND);
		}
		BeanUtils.copyProperties(userData, userLoged, "password");

		return userLoged;
	}

}
