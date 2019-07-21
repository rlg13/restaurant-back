package com.demo.restaurant.rest.api.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.demo.restaurant.rest.api.controller.beans.DishRest;
import com.demo.restaurant.rest.api.exceptions.AlreadyExistsException;
import com.demo.restaurant.rest.api.exceptions.NoDataFoundException;
import com.demo.restaurant.rest.api.service.DishService;
import com.demo.restaurant.rest.api.types.DishType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

@RunWith(SpringRunner.class)
@WebMvcTest(value = DishController.class)
@ActiveProfiles(profiles = { "test" })
public class DishControllerTest {

	private DishRest dishMock;

	private List<DishRest> dishArrayMock;

	private ObjectWriter ow;

	@Autowired
	private MockMvc mvc;

	@MockBean
	private DishService dishService;

	@Before
	public void setup() {
		DishRest dishSecondMock = new DishRest();

		dishMock = new DishRest();
		dishMock.setId(1L);
		dishMock.setName("Salad");
		dishMock.setType(DishType.FIRST);

		dishSecondMock.setId(2L);
		dishSecondMock.setName("Soup");
		dishSecondMock.setType(DishType.FIRST);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ow = mapper.writer().withDefaultPrettyPrinter();

		dishArrayMock = new ArrayList<>();

		dishArrayMock.add(dishMock);
		dishArrayMock.add(dishSecondMock);

	}

	@Test
	public void should_return_correct_dish() throws Exception {

		// ARRANGE
		doReturn(dishMock).when(dishService).getDish(any(Long.class));

		// ACT & ASSERT
		mvc.perform(get("/dishes/{id}", "1")).andExpect(status().isOk())
				.andExpect(content().json(ow.writeValueAsString(dishMock)));

	}

	@Test
	public void should_return_exception_not_found_dish() throws Exception {

		// ARRANGE
		doThrow(NoDataFoundException.class).when(dishService).getDish(any(Long.class));

		// ACT & ASSERT
		mvc.perform(get("/dishes/{id}", "1")).andExpect(status().isNotFound())
				.andExpect(status().reason(containsString(NoDataFoundException.DISH_NOT_FOUND)));
	}

	@Test
	public void should_return_exception_exists_dish() throws Exception {

		// ARRANGE
		doThrow(AlreadyExistsException.class).when(dishService).createDish(any(DishRest.class));

		// ACT & ASSERT
		mvc.perform(
				post("/dishes").contentType(MediaType.APPLICATION_JSON_UTF8).content(ow.writeValueAsString(dishMock)))
				.andExpect(status().isConflict())
				.andExpect(status().reason(containsString(AlreadyExistsException.DISH_EXISTS)));

	}

	@Test
	public void should_return_Array_of_correct_dish() throws Exception {

		// ARRANGE
		doReturn(dishArrayMock).when(dishService).getDishesByType(any(DishType.class));

		// ACT & ASSERT
		mvc.perform(get("/dishes/type/{type}", DishType.FIRST)).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", is(2)))
				.andExpect(content().json(ow.writeValueAsString(dishArrayMock)));

	}
}
