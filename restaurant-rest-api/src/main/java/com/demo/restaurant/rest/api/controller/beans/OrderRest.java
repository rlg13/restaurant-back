package com.demo.restaurant.rest.api.controller.beans;

import java.util.Date;

import com.demo.restaurant.rest.api.types.OrderState;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderRest {

	private Long id;
	
	private Date dayOrder;
	
	private Date dayToServe;
		
	private UserRest user;
	
	private OrderState state;	
	
	private DishRest firstDish;	
	
	private DishRest secondDish;	

	private DishRest dessert;
	
	
}
