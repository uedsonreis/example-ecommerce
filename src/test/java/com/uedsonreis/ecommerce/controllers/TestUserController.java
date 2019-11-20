package com.uedsonreis.ecommerce.controllers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uedsonreis.ecommerce.entities.Customer;
import com.uedsonreis.ecommerce.entities.User;
import com.uedsonreis.ecommerce.repositories.CustomerRepository;

@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestUserController extends ControllerTester {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private Integer idToDelete = null;

	@BeforeAll
	public void setup(WebApplicationContext wac) {
		super.setup(wac);
	}

	@AfterAll
	public void deleteCustomers() {
		this.customerRepository.deleteById(this.idToDelete);
	}
	
	@Test
	public void testLogin() {
		// Test verify a logged user
		try {
			ResultActions result = super.test(get("/user/logged"), status().isNoContent());
			
			String content = result.andReturn().getResponse().getContentAsString();
			assertEquals("", content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			// Test to a not registered user.
			final User wrong = new User();
			wrong.setLogin("qualquer");
			wrong.setPassword("123");
			
			ResultActions result = super.test(
					get("/user/login").param("login", wrong.getLogin()).param("password", wrong.getPassword()),
					status().isBadRequest());
			
			String content = result.andReturn().getResponse().getContentAsString();
			assertEquals("", content);

			result = super.test(
					post("/user/login").contentType("application/json").content(this.objectMapper.writeValueAsString(wrong)),
					status().isBadRequest());
			
			content = result.andReturn().getResponse().getContentAsString();
			assertEquals("", content);

			// Test to a registered user.
			final User right = new User();
			right.setLogin("admin");
			right.setPassword("admin");

			result = super.test(
					get("/user/login").param("login", right.getLogin()).param("password", right.getPassword()),
					status().isOk());
			
			content = result.andReturn().getResponse().getContentAsString();
			assertEquals("admin", content);
			
			result = super.test(
					post("/user/login").contentType("application/json").content(this.objectMapper.writeValueAsString(right)),
					status().isOk());
			
			content = result.andReturn().getResponse().getContentAsString();
			assertEquals("admin", content);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Test verify a logged user again
		try {
			ResultActions result = super.test(get("/user/logged"), status().isOk());
			String content = result.andReturn().getResponse().getContentAsString();
			assertEquals("admin", content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testAddCustomer() {
		
		User user = new User();
		user.setLogin("uedson@reis.com");
		user.setPassword("321");
		
		Customer customer = new Customer();
		customer.setAge(16);
		customer.setUser(user);
		customer.setName("Uedson Reis");
		customer.setEmail(user.getLogin());
		customer.setAddress("Rua Fulano de Tal, n. 13");
		
		try {
			// Test to a incorrect age.
			ResultActions result = super.test(
					get("/user/customer/add")
						.param("address", customer.getAddress())
						.param("age", customer.getAge().toString())
						.param("email", customer.getEmail())
						.param("name", customer.getName())
						.param("password", user.getPassword()),
					status().isBadRequest());
			
			result.andExpect(jsonPath("password").doesNotExist());

			result = super.test(
					post("/user/customer/add").contentType("application/json").content(this.objectMapper.writeValueAsString(customer)),
					status().isBadRequest());

			// Test to a correct age.
			customer.setAge(37);
			
			result = super.test(
					get("/user/customer/add")
						.param("address", customer.getAddress())
						.param("age", customer.getAge().toString())
						.param("email", customer.getEmail())
						.param("name", customer.getName())
						.param("password", user.getPassword()),
					status().isOk());
						
			final String content2 = result.andReturn().getResponse().getContentAsString();
			
			assertDoesNotThrow(() -> { Integer.valueOf(content2); });
			
			Object data = objectMapper.readValue(content2, Integer.class);
				
			this.idToDelete = (Integer) data;

			// Test to add the same customer.
			result = super.test(
					post("/user/customer/add").contentType("application/json").content(this.objectMapper.writeValueAsString(customer)),
					status().isBadRequest());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}