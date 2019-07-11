package com.demo.restaurant.restaurantbatch.job.service;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.demo.restaurant.rest.api.controller.beans.UserRest;

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
		
		if(response.getStatusCode() != HttpStatus.OK) {
			//Excepcion
		}
		
		return response.getBody();
	}
	
/*	public SessionBatch getAllOrdersToServeByDay() {
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<Foo> response = restTemplate
				  .exchange(fooResourceUrl, HttpMethod.POST, request, Foo.class);
		
		ResponseEntity<SessionBatch> response = restTemplate.postForEntity(url + apiPath + endPointLogin, session,
				SessionBatch.class);
	}*/
	
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
