package com.demo.restaurant.rest.api.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class Session {

	@Id
	@GeneratedValue
	@Column(columnDefinition = "uuid", updatable = false)
	private UUID id;
	
	@OneToOne	
	@JoinColumn(name = "user_id", nullable = false)	
	private Users user;
	
	@NotNull
	private Date expirationDate;
	
}
