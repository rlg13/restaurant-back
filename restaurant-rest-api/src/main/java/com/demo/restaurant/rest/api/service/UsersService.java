package com.demo.restaurant.rest.api.service;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.demo.restaurant.rest.api.controller.beans.UserRest;
import com.demo.restaurant.rest.api.exceptions.AlreadyExistsException;
import com.demo.restaurant.rest.api.exceptions.NoDataFoundException;
import com.demo.restaurant.rest.api.model.Users;
import com.demo.restaurant.rest.api.repository.UsersRepository;
import com.demo.restaurant.rest.api.utils.Constants;
import com.demo.restaurant.rest.utils.CypherText;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@Service
public class UsersService {

	
	private UsersRepository usersRepository;

	public UserRest createUser(UserRest user) throws AlreadyExistsException, NoSuchAlgorithmException {
		Users newUser = new Users();
		UserRest returnUser = new UserRest();

		try {
			BeanUtils.copyProperties(user, newUser);
			newUser.setPassword(CypherText.sha256(user.getPassword()));
			if(newUser.getEnableSystemOperations() == null) {
				newUser.setEnableSystemOperations(Boolean.FALSE);
			}
			newUser = usersRepository.save(newUser);			
			BeanUtils.copyProperties(newUser, returnUser,Constants.PASSWORD);
			return returnUser;
		} catch (DataIntegrityViolationException exp) {
			throw new AlreadyExistsException(AlreadyExistsException.USER_EXISTS);
		}

	}

	public UserRest getUser(@NonNull UserRest user) throws NoDataFoundException, NoSuchAlgorithmException {
		UserRest userLoged = new UserRest();

		Users userData = usersRepository.findOneByNameAndPassword(user.getName(), CypherText.sha256(user.getPassword()));
		if (userData == null) {
			throw new NoDataFoundException(NoDataFoundException.USER_NOT_FOUND);
		}
		
		BeanUtils.copyProperties(userData, userLoged, Constants.PASSWORD);

		return userLoged;
	}

}
