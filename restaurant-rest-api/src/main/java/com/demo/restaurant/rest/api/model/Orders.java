package com.demo.restaurant.rest.api.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.demo.restaurant.rest.api.types.OrderState;

import lombok.Data;

@Data
@Entity
public class Orders {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 
	
	@ManyToOne
	@NotNull
	@JoinColumn(name = "user_id")	
	private Users user;
	
	@NotNull
	private Date dayOrder;
	
	@NotNull
	private Date dayToServe;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private OrderState state;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "first_dish_id", nullable = true)	
	private DishCatalog firstDish;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "second_dish_id", nullable = true)
	private DishCatalog secondDish;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "desert_id", nullable = true)
	private DishCatalog dessert;
	

	
	
	
}
