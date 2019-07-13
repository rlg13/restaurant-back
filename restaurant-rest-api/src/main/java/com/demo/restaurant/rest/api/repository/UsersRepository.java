package com.demo.restaurant.rest.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.restaurant.rest.api.model.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

	public Users findOneByName(String name);

	public Users findOneByNameAndPassword(String name, String password);

}
