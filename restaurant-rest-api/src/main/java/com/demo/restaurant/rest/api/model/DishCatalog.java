package com.demo.restaurant.rest.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.demo.restaurant.rest.api.types.DishType;

import lombok.Data;

@Data
@Entity
public class DishCatalog {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private DishType type;
	
	@Column(length = 100, unique = true)
	@NotNull
	private String name;

}
