package com.demo.restaurant.rest.api.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class ProjectVars {

	@Value("${filter.max-days:45}")
	private Integer maxDaysFilter;
	
	@Value("${session.max-miliseconds:300000}")
	private Integer milisecondsToExpireSession;	

}
