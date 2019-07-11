package com.demo.restaurant.rest.api.controller.beans;

import com.demo.restaurant.rest.api.types.DishType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DishRest {

	private Long id;
	
	private String name;
	
	private DishType type;
}
