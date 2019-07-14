package com.demo.restaurant.rest.api.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.dao.DataIntegrityViolationException;

import com.demo.restaurant.rest.api.controller.beans.DishRest;
import com.demo.restaurant.rest.api.exceptions.AlreadyExistsException;
import com.demo.restaurant.rest.api.exceptions.NoDataFoundException;
import com.demo.restaurant.rest.api.model.DishCatalog;
import com.demo.restaurant.rest.api.repository.DishCatalogRepository;
import com.demo.restaurant.rest.api.types.DishType;

@RunWith(MockitoJUnitRunner.class)
public class DishServiceTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	private DishCatalogRepository dishCatalogRepository;

	@InjectMocks
	private DishService dishService;

	@Test
	public void should_return_new_dish() throws AlreadyExistsException {
		// ARRANGE
		DishRest dish = new DishRest();
		dish.setName("Salad");
		dish.setType(DishType.FIRST);

		when(dishCatalogRepository.save(any(DishCatalog.class))).then(new Answer<DishCatalog>() {
			public DishCatalog answer(InvocationOnMock invocation) throws Throwable {
				DishCatalog newDish = (DishCatalog) invocation.getArguments()[0];
				newDish.setId(2L);
				return newDish;
			}
		});

		// ACT
		DishRest newDish = dishService.createDish(dish);

		// ASSERT
		assertNotNull(newDish);
		assertEquals(Long.valueOf(2l), newDish.getId());
		assertEquals(dish.getName(), newDish.getName());
		assertEquals(dish.getType(), newDish.getType());
	}

	@Test
	public void should_return_exception_dish_duplicate() throws AlreadyExistsException {
		// ARRANGE
		DishRest dish = new DishRest();
		dish.setName("Salad");
		dish.setType(DishType.FIRST);

		when(dishCatalogRepository.save(any(DishCatalog.class))).thenThrow(DataIntegrityViolationException.class);

		// ASSERT
		thrown.expect(AlreadyExistsException.class);
		thrown.expectMessage(AlreadyExistsException.DISH_EXISTS);

		// ACT
		dishService.createDish(dish);
		fail();

	}

	@Test
	public void should_return_exception_if_element_not_exists() throws NoDataFoundException {
		// ARRANGE
		Long id = Long.valueOf(1L);

		when(dishCatalogRepository.findById(any(Long.class))).thenThrow(NoSuchElementException.class);
		// ASSERT
		thrown.expect(NoDataFoundException.class);
		thrown.expectMessage(NoDataFoundException.DISH_NOT_FOUND);

		// ACT
		dishService.getDish(id);
		fail();
	}

	@Test
	public void should_return_dish() throws NoDataFoundException {
		// ARRANGE
		Long id = Long.valueOf(1L);

		DishCatalog dish = new DishCatalog();
		dish.setId(1L);
		dish.setName("Salad");
		dish.setType(DishType.FIRST);
		Optional<DishCatalog> dishOptional = Optional.of(dish);

		when(dishCatalogRepository.findById(any(Long.class))).thenReturn(dishOptional);

		// ACT
		DishRest dishRest = dishService.getDish(id);

		// ASSERT
		assertNotNull(dishRest);

		assertEquals(id, dishRest.getId());
		assertEquals(dish.getName(), dishRest.getName());
		assertEquals(dish.getType(), dishRest.getType());

	}

	@Test
	public void should_return_list_dish() throws NoDataFoundException {
		// ARRANGE

		DishCatalog dish = new DishCatalog();
		dish.setId(1L);
		dish.setName("Salad");
		dish.setType(DishType.FIRST);
		List<DishCatalog> array = new ArrayList<>();
		array.add(dish);

		when(dishCatalogRepository.findByType(any(DishType.class))).thenReturn(array);

		// ACT
		List<DishRest> arrayDishes = dishService.getDishesByType(DishType.FIRST);

		// ASSERT
		assertNotNull(arrayDishes);
		assertEquals(1, arrayDishes.size());
		DishRest dishRest = arrayDishes.get(0);
		assertNotNull(dishRest);
		assertEquals(dish.getId(), dishRest.getId());
		assertEquals(dish.getName(), dishRest.getName());
		assertEquals(dish.getType(), dishRest.getType());

	}

}
