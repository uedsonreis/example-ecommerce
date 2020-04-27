package com.uedsonreis.ecommerce.controllers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uedsonreis.ecommerce.entities.Factory;
import com.uedsonreis.ecommerce.entities.Product;
import com.uedsonreis.ecommerce.entities.User;
import com.uedsonreis.ecommerce.services.ProductService;
import com.uedsonreis.ecommerce.utils.Util;

@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestProductController extends ControllerTester {

	private String token;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ProductService productService;
	
	private Integer idsToDelete[] = new Integer[2];

	@BeforeAll
	public void setup(WebApplicationContext wac) {
		super.setup(wac);
	}
	
	@BeforeAll
	public void addSomeProducts() {
		Factory microsoft = new Factory();
		microsoft.setName("Microsoft");
		
		Product nootebook = new Product();
		nootebook.setAmount(5);
		nootebook.setName("Microsoft Notebook");
		nootebook.setPrice(6399.0);
		nootebook.setFactory(microsoft);
		
		try {
			this.productService.save(nootebook);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAll() {
		this.testNotLogged();
		this.testAddProduct();
		this.testListProducts();
		this.testRemoveProduct();
	}
	
	private void testNotLogged() {
		try {
			Factory apple = new Factory();
			apple.setName("Apple");
			
			Product macBook = new Product();
			macBook.setAmount(5);
			macBook.setName("MackBook Pro");
			macBook.setPrice(14399.0);
			macBook.setFactory(apple);
			
			super.test(
					post("/product/add").contentType("application/json").content(this.objectMapper.writeValueAsString(macBook)),
					status().isUnauthorized());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void testAddProduct() {
		try {
			final User admin = new User();
			admin.setLogin("admin");
			admin.setPassword("admin");
			
			ResultActions result = super.test(
					post("/user/login").contentType("application/json").content(this.objectMapper.writeValueAsString(admin)),
					status().isOk());
			
			this.token = result.andReturn().getResponse().getContentAsString();
			assertNotEquals("", this.token);
			
			Factory apple = new Factory();
			apple.setName("Apple");
			
			Product iPhone = new Product();
			iPhone.setAmount(10);
			iPhone.setName("iPhone 11");
			iPhone.setPrice(4399.0);
			iPhone.setFactory(apple);
			
			result = super.test(
					post("/product/add")
						.header(Util.AUTH, this.treatToken(this.token))
						.contentType("application/json")
						.content(this.objectMapper.writeValueAsString(iPhone)),
					status().isOk());
			
			final String content3 = result.andReturn().getResponse().getContentAsString();
			assertDoesNotThrow(() -> { Integer.valueOf(content3); });
			
			Object data = objectMapper.readValue(content3.toString(), Integer.class);
				
			this.idsToDelete[0] = (Integer) data;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void testListProducts() {
		try {
			ResultActions result = super.test(get("/product/list"), status().isOk());
			
			String content = result.andReturn().getResponse().getContentAsString();
			
			assertNotEquals("", content);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void testRemoveProduct() {
		try {
			this.mockMvc.perform(delete("/product/"+this.idsToDelete[0].toString()+"/remove")
					.header(Util.AUTH, this.treatToken(this.token)))
				.andDo(print()).andExpect(status().isOk());
			
			this.mockMvc.perform(delete("/product/-1/remove")
					.header(Util.AUTH, this.treatToken(this.token)))
				.andDo(print()).andExpect(status().isNotFound());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}