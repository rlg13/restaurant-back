package com.demo.restaurant.rest.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.restaurant.rest.api.model.DishCatalog;
import com.demo.restaurant.rest.api.types.DishType;

@Repository
public interface DishCatalogRepository extends JpaRepository<DishCatalog, Long> {
	
	public List<DishCatalog> findByType(DishType type);

}
