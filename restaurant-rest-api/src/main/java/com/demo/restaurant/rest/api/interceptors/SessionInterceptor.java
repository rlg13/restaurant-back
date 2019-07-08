package com.demo.restaurant.rest.api.interceptors;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import com.demo.restaurant.rest.api.exceptions.NoDataFoundException;
import com.demo.restaurant.rest.api.service.SessionService;
import com.demo.restaurant.rest.api.utils.Constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SessionInterceptor implements HandlerInterceptor{

	private SessionService sessionService;
	
	private List<String> authUriList;
	
	@Value("${server.servlet.context-path}")
	private String apiBase;
	
	public SessionInterceptor(SessionService sessionService) {
		this.sessionService = sessionService;	
	}
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String sesionID = request.getHeader(Constants.HEADER_SESSION);
		UUID uuid;
		if(checkPublicUri(request.getRequestURI()) || request.getMethod().equals("OPTIONS")) {
			return HandlerInterceptor.super.preHandle(request, response, handler);
		}
		
		if(sesionID == null) {
			log.error("Session not exists");
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, Constants.INTERCEPTOR_EXCEPTION_MESSAGE);
		}
		uuid = UUID.fromString(sesionID); 
		try {
			if(!sessionService.validateSession(uuid)) {
				log.error("Call with session invalid");
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, Constants.INTERCEPTOR_EXCEPTION_MESSAGE);				
			}
		} catch (NoDataFoundException e) {
			log.error("Session not exists");
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, Constants.INTERCEPTOR_EXCEPTION_MESSAGE);
		}
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	
	private boolean checkPublicUri(String requestUri) {
		if(authUriList == null) {
			inicialize();
		}
		return authUriList.contains(requestUri);
	}
	
	private void inicialize() {
		authUriList = new ArrayList<>();
		authUriList.add(apiBase+"/users");
		authUriList.add(apiBase+"/users/login");
		authUriList.add(apiBase+"/error");
	}
	
}
