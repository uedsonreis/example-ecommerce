package com.uedsonreis.ecommerce.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uedsonreis.ecommerce.entities.Customer;
import com.uedsonreis.ecommerce.entities.User;

@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestUserController {

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@BeforeAll
	void setup(WebApplicationContext wac) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(sharedHttpSession()).build();
	}
	
	@Test
	public void testLogin() {
		
		// Test verify a logged user
		try {
			this.test(get("/user/logged"), false, jsonPath("data").doesNotExist());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			// Test to a not registered user.
			final User wrong = new User();
			wrong.setLogin("qualquer");
			wrong.setPassword("123");
			
			this.test(
					get("/user/login").param("login", wrong.getLogin()).param("password", wrong.getPassword()),
					false, jsonPath("data").doesNotExist());

			this.test(
					post("/user/login").contentType("application/json").content(this.objectMapper.writeValueAsString(wrong)),
					false, jsonPath("data").doesNotExist());

			// Test to a registered user.
			final User right = new User();
			right.setLogin("admin");
			right.setPassword("admin");

			this.test(
					get("/user/login").param("login", right.getLogin()).param("password", right.getPassword()),
					true, jsonPath("data").isString());
			
			this.test(
					post("/user/login").contentType("application/json").content(this.objectMapper.writeValueAsString(right)),
					true, jsonPath("data").isString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Test verify a logged user again
		try {
			this.test(get("/user/logged"), true, jsonPath("data").isString());
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
			this.test(
					get("/user/customer/add")
						.param("address", customer.getAddress())
						.param("age", customer.getAge().toString())
						.param("email", customer.getEmail())
						.param("name", customer.getName())
						.param("password", user.getPassword()),
					false, jsonPath("data").doesNotExist());

			this.test(
					post("/user/customer/add").contentType("application/json").content(this.objectMapper.writeValueAsString(customer)),
					false, jsonPath("data").doesNotExist());

			// Test to a correct age.
			customer.setAge(37);
			
			this.test(
					get("/user/customer/add")
						.param("address", customer.getAddress())
						.param("age", customer.getAge().toString())
						.param("email", customer.getEmail())
						.param("name", customer.getName())
						.param("password", user.getPassword()),
					true, jsonPath("data").isNumber());

			// Test to add the same customer.
			this.test(
					post("/user/customer/add").contentType("application/json").content(this.objectMapper.writeValueAsString(customer)),
					false, jsonPath("data").doesNotExist());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void test(RequestBuilder requestBuilder, Object success, ResultMatcher matcher) throws Exception {
		this.mockMvc.perform(requestBuilder)
			.andDo(print()).andExpect(status().isOk())
			.andExpect(jsonPath("success").value(success))
			.andExpect(matcher);
	}

}