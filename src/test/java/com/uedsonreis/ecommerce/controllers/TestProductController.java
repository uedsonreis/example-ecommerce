package com.uedsonreis.ecommerce.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uedsonreis.ecommerce.entities.Factory;
import com.uedsonreis.ecommerce.entities.Product;
import com.uedsonreis.ecommerce.entities.User;

@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestProductController extends ControllerTester {

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeAll
	public void setup(WebApplicationContext wac) {
		super.setup(wac);
	}
	
	@Test
	public void testAll() {
		this.testNotAddProduct();
		this.testAddProduct();
		this.testListProducts();
		this.testRemoveProduct();
	}
	
	private void testNotAddProduct() {
		try {
			Factory apple = new Factory();
			apple.setName("Apple Inc.");
			
			Product macBook = new Product();
			macBook.setAmount(5);
			macBook.setName("MackBook Pro");
			macBook.setPrice(14399.0);
			macBook.setFactory(apple);
			
			super.test(
					post("/product/add").contentType("application/json").content(this.objectMapper.writeValueAsString(macBook)),
					false, jsonPath(RETURNED).doesNotExist());
			
			super.test(
					get("/product/add")
						.param("amount", macBook.getAmount().toString())
						.param("factory", apple.getName())
						.param("name", macBook.getName())
						.param("price", macBook.getPrice().toString()),
					false, jsonPath(RETURNED).doesNotExist());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void testAddProduct() {
		try {
			final User admin = new User();
			admin.setLogin("admin");
			admin.setPassword("admin");
			
			super.test(
					get("/user/login").param("login", admin.getLogin()).param("password", admin.getPassword()),
					true, jsonPath(RETURNED).isString());
			
			Factory apple = new Factory();
			apple.setName("Apple Inc.");
			
			Product iPhone = new Product();
			iPhone.setAmount(10);
			iPhone.setName("iPhone 11");
			iPhone.setPrice(4399.0);
			iPhone.setFactory(apple);
			
			super.test(
					get("/product/add")
						.param("amount", iPhone.getAmount().toString())
						.param("factory", apple.getName())
						.param("name", iPhone.getName())
						.param("price", iPhone.getPrice().toString()),
					true, jsonPath(RETURNED).isNumber());

			super.test(
					post("/product/add").contentType("application/json").content(this.objectMapper.writeValueAsString(iPhone)),
					true, jsonPath(RETURNED).isNumber());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void testListProducts() {
		try {
			super.test(get("/product/list"), true, jsonPath(RETURNED).isArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void testRemoveProduct() {
		try {
			this.mockMvc.perform(get("/product/remove").param("id", "1"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("success").value(true));
			
			this.mockMvc.perform(get("/product/remove").param("id", "35"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("success").value(false));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}