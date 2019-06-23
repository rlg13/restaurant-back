package com.demo.restaurant.rest.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.restaurant.rest.api.model.Orders;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

}
