package com.demo.restaurant.rest.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.restaurant.rest.api.model.Session;

public interface SessionRepository extends JpaRepository<Session, UUID> {

}
