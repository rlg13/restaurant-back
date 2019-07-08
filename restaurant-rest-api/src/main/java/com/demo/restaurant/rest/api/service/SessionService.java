package com.demo.restaurant.rest.api.service;

import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.restaurant.rest.api.controller.beans.UserRest;
import com.demo.restaurant.rest.api.exceptions.NoDataFoundException;
import com.demo.restaurant.rest.api.model.Session;
import com.demo.restaurant.rest.api.model.Users;
import com.demo.restaurant.rest.api.repository.SessionRepository;
import com.demo.restaurant.rest.api.utils.Constants;

import lombok.NonNull;

@Service
public class SessionService {

	

	@Autowired
	SessionRepository sessionRepository;

	public Session createSession(UserRest user) {
		Session session = new Session();
		Users userModel = new Users();

		userModel.setId(user.getId());
		session.setUser(userModel);
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MILLISECOND, Constants.MILISECONDS_TO_EXPIRE);
		session.setExpirationDate(now.getTime());
		session = sessionRepository.save(session);

		return session;

	}

	public void invalidateSession(@NonNull UUID sessionId) {
		sessionRepository.deleteById(sessionId);
	}

	public boolean validateSession(@NonNull UUID sessionId) throws NoDataFoundException {
		try {
			Session session = sessionRepository.findById(sessionId).orElseThrow();
			Calendar now = Calendar.getInstance();
			return now.getTime().before(session.getExpirationDate());
		} catch (NoSuchElementException e) {
			throw new NoDataFoundException(NoDataFoundException.SESSION_NOT_FOUND);
		}
	}

}
