package com.demo.restaurant.rest.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@Column(length = 50, nullable = false)
	private String name;
	
	@Column(length = 64, nullable = false)	
	private String password;

	@Column(columnDefinition = "boolean default false")
	private Boolean enableSystemOperations;
}
