package com.uedsonreis.ecommerce.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;

import org.json.JSONObject;
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
import com.uedsonreis.ecommerce.entities.Item;
import com.uedsonreis.ecommerce.entities.Product;

@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestCartController extends ControllerTester {

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeAll
	public void setup(WebApplicationContext wac) {
		super.setup(wac);
	}
	
	@SuppressWarnings("unchecked")
	private void testAddInCart() throws Exception {
		ResultActions result = this.mockMvc.perform(get("/product/list"))
			.andDo(print()).andExpect(status().isOk())
			.andExpect(jsonPath("success").value(true))
			.andExpect(jsonPath(RETURNED).isArray());
			
		Collection<Product> products = (Collection<Product>) new JSONObject(result.andReturn().getResponse().getContentAsString()).get("data");
		
		Product product1 = products.iterator().next();
		
		super.test(
				get("/cart/add")
					.param("productId", product1.getId().toString())
					.param("amount", "1")
					.param("price", product1.getPrice().toString()),
				true, jsonPath(RETURNED).doesNotExist());
		
		Product product2 = products.iterator().next();
		
		Item item = new Item();
		item.setAmount(2);
		item.setProduct(product2);
		item.setPrice(product2.getPrice() * 0.9);

		super.test(
				post("/cart/add").contentType("application/json").content(this.objectMapper.writeValueAsString(item)),
				true, jsonPath(RETURNED).doesNotExist());			
	}
	
	@Test
	public void testAll() {
		try {
			super.test(get("/cart/list"), false, jsonPath(RETURNED).doesNotExist());
			
			this.testAddInCart();
			
			super.test(get("/cart/list"), true, jsonPath(RETURNED).isArray());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}