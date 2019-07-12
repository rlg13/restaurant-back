package com.demo.restaurant.restaurantbatch.job.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.demo.restaurant.rest.api.controller.beans.OrderRest;
import com.demo.restaurant.rest.api.controller.beans.UserRest;
import com.demo.restaurant.rest.api.types.OrderState;
import com.demo.restaurant.restaurantbatch.job.exception.ProcessException;
import com.demo.restaurant.restaurantbatch.job.utils.Constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CallApiService {

	@Value("${api.url}")
	private String url;

	@Value("${api.api-path}")
	private String apiPath;

	@Value("${api.endpoint-login}")
	private String endPointLogin;

	@Value("${api.endpoint-filterOrder}")
	private String endpointFilterOrder;

	@Value("${api.endpoint-processOrder}")
	private String endpointProcessOrder;

	@Value("${api.login.name}")
	private String name;

	@Value("${api.login.password}")
	private String password;

	public UserRest stablishSession() {
		RestTemplate restTemplate = new RestTemplate();

//		MessageDigest digest = MessageDigest.getInstance("SHA-256");
//		byte[] hash = digest.digest("aaaa".getBytes(StandardCharsets.UTF_8));
//		String endode = bytesToHex(hash);
		UserRest session = new UserRest();
		session.setName(name);
		session.setPassword(password);

		ResponseEntity<UserRest> response = restTemplate.postForEntity(url + apiPath + endPointLogin, session,
				UserRest.class);

		if (response.getStatusCode() != HttpStatus.OK) {
			// Excepcion
		}

		return response.getBody();
	}

	public List<OrderRest> getAllOrdersToServeByDay(String sessionId) {
		RestTemplate restTemplate = new RestTemplate();

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add(Constants.HEADER_SESSION, sessionId);
		HttpEntity<Void> entity = new HttpEntity<>(headers);

		ResponseEntity<List<OrderRest>> response = restTemplate.exchange(url + apiPath + endpointFilterOrder,
				HttpMethod.GET, entity, new ParameterizedTypeReference<List<OrderRest>>() {
				});

		return response.getBody();
	}

	public OrderRest processOrder(OrderRest order, String sessionId) throws ProcessException {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set(Constants.HEADER_SESSION, sessionId);
		HttpEntity<Void> entity = new HttpEntity<>(headers);
		String endPoint = String.format(endpointProcessOrder, order.getId(), OrderState.DELIVERED);
		ResponseEntity<OrderRest> response = restTemplate.exchange(url + apiPath + endPoint, HttpMethod.GET, entity,
				OrderRest.class);
		if (response.getStatusCode() != HttpStatus.OK) {
			throw new ProcessException(String.format(ProcessException.ERROR_PROCESS, order.getId()));
		}
		return response.getBody();
	}

//	private static String bytesToHex(byte[] hash) {
//	    StringBuffer hexString = new StringBuffer();
//	    for (int i = 0; i < hash.length; i++) {
//	    String hex = Integer.toHexString(0xff & hash[i]);
//	    if(hex.length() == 1) hexString.append('0');
//	        hexString.append(hex);
//	    }
//	    return hexString.toString();
//	}
}
