package com.demo.restaurant.rest.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class Users {

	@Id
	@Column(length = 50)
	private String name;
	
	@Column(length = 64)
	@NotNull
	private String password;
	
}
