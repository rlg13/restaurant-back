package com.demo.restaurant.rest.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.demo.restaurant.rest.api.interceptors.SessionInterceptor;

@Configuration
@Profile(value = {"local","docker","pro"})
public class ControllerConfiguration implements WebMvcConfigurer {

	@Autowired
	private SessionInterceptor sessionInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(sessionInterceptor);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
				.allowedHeaders("Authorization", "authorization", "content-type")
				.exposedHeaders("Authorization", "authorization", "content-type").allowCredentials(true).maxAge(3600);

	}

}
