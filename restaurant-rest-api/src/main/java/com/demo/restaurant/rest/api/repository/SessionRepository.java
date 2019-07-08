package com.demo.restaurant.rest.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.restaurant.rest.api.model.Session;
import com.demo.restaurant.rest.api.model.Users;

public interface SessionRepository extends JpaRepository<Session, UUID> {

	List<Session> findByUser(Users user);
}
