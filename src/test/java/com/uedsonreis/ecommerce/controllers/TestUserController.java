package com.uedsonreis.ecommerce.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.uedsonreis.ecommerce.repositories.UserRepository;
import com.uedsonreis.ecommerce.utils.Util;

@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestUserController extends ControllerTester {
	
	private String token;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@BeforeAll
	public void setup(WebApplicationContext wac) {
		super.setup(wac);
	}

	@AfterAll
	public void deleteCustomers() {
		for (Customer c: this.customerRepository.findAll()) {
			this.customerRepository.delete(c);
			this.userRepository.delete(c.getUser());
		}
	}
	
	@Test
	public void testLogin() {
		// Test verify a logged user
		try {
			super.test(get("/user/logged").header(Util.AUTH, "token"), status().isNoContent());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			// Test to a not registered user.
			final User wrong = new User();
			wrong.setLogin("qualquer");
			wrong.setPassword("123");

			super.test(
					post("/user/login").contentType("application/json")
					.content(this.objectMapper.writeValueAsString(wrong)),
					status().isUnauthorized());

			// Test to a registered user.
			final User right = new User();
			right.setLogin("admin");
			right.setPassword("admin");
			
			ResultActions result = super.test(
					post("/user/login").contentType("application/json").content(this.objectMapper.writeValueAsString(right)),
					status().isOk());
			
			this.token = result.andReturn().getResponse().getContentAsString();
			assertNotEquals("", this.token);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Test verify a logged user again
		try {
			ResultActions result = super.test(get("/user/logged").header(Util.AUTH, this.treatToken(this.token)), status().isOk());
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
					post("/user/customer/add").contentType("application/json").content(this.objectMapper.writeValueAsString(customer)),
					status().isBadRequest());

			// Test to a correct age.
			customer.setAge(37);
			
			result = super.test(
					post("/user/customer/add").contentType("application/json").content(this.objectMapper.writeValueAsString(customer)),
					status().isCreated());
			
			this.token = result.andReturn().getResponse().getContentAsString();
			assertNotEquals("", this.token);

			// Test to add the same customer.
			result = super.test(
					post("/user/customer/add").contentType("application/json").content(this.objectMapper.writeValueAsString(customer)),
					status().isBadRequest());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}