package com.demo.restaurant.rest.api.controller.beans;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FilterParams {
	
	private Date inicialDate;
	
	private Date endDate;
	
	private UserRest user;

}
