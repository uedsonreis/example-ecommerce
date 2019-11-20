package com.uedsonreis.ecommerce.controllers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestProductController extends ControllerTester {

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
			apple.setName("Apple Inc.");
			
			Product macBook = new Product();
			macBook.setAmount(5);
			macBook.setName("MackBook Pro");
			macBook.setPrice(14399.0);
			macBook.setFactory(apple);
			
			ResultActions result = super.test(
					post("/product/add").contentType("application/json").content(this.objectMapper.writeValueAsString(macBook)),
					status().isUnauthorized());
			
			String content = result.andReturn().getResponse().getContentAsString();
			assertEquals("", content);
			
			super.test(
					get("/product/add")
						.param("amount", macBook.getAmount().toString())
						.param("factory", apple.getName())
						.param("name", macBook.getName())
						.param("price", macBook.getPrice().toString()),
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
					get("/user/login").param("login", admin.getLogin()).param("password", admin.getPassword()),
					status().isOk());
			
			String content = result.andReturn().getResponse().getContentAsString();
			assertEquals(admin.getLogin(), content);
			
			Factory apple = new Factory();
			apple.setName("Apple Inc.");
			
			Product iPhone = new Product();
			iPhone.setAmount(10);
			iPhone.setName("iPhone 11");
			iPhone.setPrice(4399.0);
			iPhone.setFactory(apple);
			
			result = super.test(
					get("/product/add")
						.param("amount", iPhone.getAmount().toString())
						.param("factory", apple.getName())
						.param("name", iPhone.getName())
						.param("price", iPhone.getPrice().toString()),
					status().isOk());

			final String content2 = result.andReturn().getResponse().getContentAsString();
			assertDoesNotThrow(() -> { Integer.valueOf(content2); });
			
			Object data = objectMapper.readValue(content2.toString(), Integer.class);
				
			this.idsToDelete[0] = (Integer) data;
			
			result = super.test(
					post("/product/add").contentType("application/json").content(this.objectMapper.writeValueAsString(iPhone)),
					status().isOk());
			
			final String content3 = result.andReturn().getResponse().getContentAsString();
			assertDoesNotThrow(() -> { Integer.valueOf(content3); });
			
			data = objectMapper.readValue(content3.toString(), Integer.class);
				
			this.idsToDelete[1] = (Integer) data;
			
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
			this.mockMvc.perform(delete("/product/"+this.idsToDelete[0].toString()+"/remove"))
				.andDo(print()).andExpect(status().isOk());
			
			this.mockMvc.perform(delete("/product/"+this.idsToDelete[1].toString()+"/remove"))
				.andDo(print()).andExpect(status().isOk());
			
			this.mockMvc.perform(delete("/product/-1/remove"))
				.andDo(print()).andExpect(status().isNoContent());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}