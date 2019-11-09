package com.uedsonreis.ecommerce.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.json.JSONObject;
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
			super.test(get("/user/logged"), false, jsonPath(RETURNED).doesNotExist());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			// Test to a not registered user.
			final User wrong = new User();
			wrong.setLogin("qualquer");
			wrong.setPassword("123");
			
			super.test(
					get("/user/login").param("login", wrong.getLogin()).param("password", wrong.getPassword()),
					false, jsonPath(RETURNED).doesNotExist());

			super.test(
					post("/user/login").contentType("application/json").content(this.objectMapper.writeValueAsString(wrong)),
					false, jsonPath(RETURNED).doesNotExist());

			// Test to a registered user.
			final User right = new User();
			right.setLogin("admin");
			right.setPassword("admin");

			super.test(
					get("/user/login").param("login", right.getLogin()).param("password", right.getPassword()),
					true, jsonPath(RETURNED).isString());
			
			super.test(
					post("/user/login").contentType("application/json").content(this.objectMapper.writeValueAsString(right)),
					true, jsonPath(RETURNED).isString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Test verify a logged user again
		try {
			super.test(get("/user/logged"), true, jsonPath(RETURNED).isString());
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
			super.test(
					get("/user/customer/add")
						.param("address", customer.getAddress())
						.param("age", customer.getAge().toString())
						.param("email", customer.getEmail())
						.param("name", customer.getName())
						.param("password", user.getPassword()),
					false, jsonPath(RETURNED).doesNotExist());

			super.test(
					post("/user/customer/add").contentType("application/json").content(this.objectMapper.writeValueAsString(customer)),
					false, jsonPath(RETURNED).doesNotExist());

			// Test to a correct age.
			customer.setAge(37);
			
			ResultActions result = super.test(
					get("/user/customer/add")
						.param("address", customer.getAddress())
						.param("age", customer.getAge().toString())
						.param("email", customer.getEmail())
						.param("name", customer.getName())
						.param("password", user.getPassword()),
					true, jsonPath(RETURNED).isNumber());
			
			String content = result.andReturn().getResponse().getContentAsString();
			
			Object data = objectMapper.readValue(new JSONObject(content).get(RETURNED).toString(), Integer.class);
				
			this.idToDelete = (Integer) data;

			// Test to add the same customer.
			super.test(
					post("/user/customer/add").contentType("application/json").content(this.objectMapper.writeValueAsString(customer)),
					false, jsonPath(RETURNED).doesNotExist());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}