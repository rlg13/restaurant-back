package com.demo.restaurant.rest.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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

	public List<UserRest> getAllUsers() {

		List<UserRest> allUser = new ArrayList<>();

		for (Users temp : usersRepository.findAll()) {
			UserRest dest = new UserRest();
			BeanUtils.copyProperties(temp, dest);
			allUser.add(dest);
		}

		return allUser;
	}

	public UserRest createUser(UserRest user) throws AlreadyExistsException {
		Users newUser = new Users();
		BeanUtils.copyProperties(user, newUser);
		try {
			newUser = usersRepository.save(newUser);
			user.setId(newUser.getId());
			return user;
		} catch (DataIntegrityViolationException exp) { // User name exists
			throw new AlreadyExistsException(AlreadyExistsException.USER_EXISTS);
		}

	}

	public UserRest getUser(@NonNull Long idUser) throws NoDataFoundException {
		UserRest user = new UserRest();
		try {
			Users userData = usersRepository.findById(idUser).orElseThrow();
			BeanUtils.copyProperties(userData, user);
		} catch (NoSuchElementException e) {
			throw new NoDataFoundException(NoDataFoundException.USER_NOT_FOUND);
		}

		return user;
	}

	public UserRest getUser(@NonNull String name) throws NoDataFoundException {
		UserRest user = new UserRest();

		Users userData = usersRepository.findOneByName(name);
		if(userData == null) {
			throw new NoDataFoundException(NoDataFoundException.USER_NOT_FOUND);
		}
		BeanUtils.copyProperties(userData, user);

		return user;
	}

}
