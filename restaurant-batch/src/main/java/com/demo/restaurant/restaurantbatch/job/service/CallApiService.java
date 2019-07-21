package com.demo.restaurant.restaurantbatch.job.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.demo.restaurant.rest.api.controller.beans.OrderRest;
import com.demo.restaurant.rest.api.controller.beans.UserRest;
import com.demo.restaurant.rest.api.types.OrderState;
import com.demo.restaurant.restaurantbatch.job.exception.ProcessException;
import com.demo.restaurant.restaurantbatch.job.utils.Constants;

@Service
public class CallApiService {

	@Value("${api.url}")
	private String url;

	@Value("${api.api-path}")
	private String apiPath;

	@Value("${api.endpoint-login}")
	private String endPointLogin;

	@Value("${api.endpoint-logout}")
	private String endPointLogout;

	@Value("${api.endpoint-filterOrder}")
	private String endpointFilterOrder;

	@Value("${api.endpoint-processOrder}")
	private String endpointProcessOrder;

	@Value("${api.login.name}")
	private String name;

	@Value("${api.login.password}")
	private String password;

	public UserRest stablishSession() throws ProcessException {
		RestTemplate restTemplate = new RestTemplate();
		UserRest session = new UserRest();
		session.setName(name);
		session.setPassword(password);

		ResponseEntity<UserRest> response = restTemplate.postForEntity(url + apiPath + endPointLogin, session,
				UserRest.class);

		if (response.getStatusCode() != HttpStatus.OK) {
			throw new ProcessException(ProcessException.STABLISH_CONNECTION);
		}

		return response.getBody();
	}

	public void endSession(UserRest user) {
		RestTemplate restTemplate = new RestTemplate();
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url + apiPath + endPointLogout)
				.queryParam("id", user.getId());

		restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.DELETE, createHeaders(user.getSessionId()),
				Void.class);

	}

	public List<OrderRest> getAllOrdersToServeByDay(String sessionId) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<OrderRest>> response = restTemplate.exchange(url + apiPath + endpointFilterOrder,
				HttpMethod.GET, createHeaders(sessionId), new ParameterizedTypeReference<List<OrderRest>>() {
				});

		return response.getBody();
	}

	public OrderRest processOrder(OrderRest order, String sessionId) throws ProcessException {
		RestTemplate restTemplate = new RestTemplate();
		String endPoint = String.format(endpointProcessOrder, order.getId(), OrderState.DELIVERED);
		ResponseEntity<OrderRest> response = restTemplate.exchange(url + apiPath + endPoint, HttpMethod.GET,
				createHeaders(sessionId), OrderRest.class);
		if (response.getStatusCode() != HttpStatus.OK) {
			throw new ProcessException(String.format(ProcessException.ERROR_PROCESS, order.getId()));
		}
		return response.getBody();
	}

	private HttpEntity<Void> createHeaders(String sessionId) {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add(Constants.HEADER_SESSION, sessionId);
		return new HttpEntity<>(headers);
	}

}
