package com.demo.restaurant.rest.api.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.restaurant.rest.api.model.Orders;
import com.demo.restaurant.rest.api.model.Users;
import com.demo.restaurant.rest.api.types.OrderState;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

	List<Orders> findByUserAndDayOrderBetween(Users user, Date initialDate, Date endDate);
	List<Orders> findByStateAndDayToServe(OrderState state, Date dayToServe);
	
}
