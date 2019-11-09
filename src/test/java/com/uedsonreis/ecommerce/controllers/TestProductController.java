package com.uedsonreis.ecommerce.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
		
		this.productService.save(nootebook);
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
			
			ResultActions result = super.test(
					get("/product/add")
						.param("amount", iPhone.getAmount().toString())
						.param("factory", apple.getName())
						.param("name", iPhone.getName())
						.param("price", iPhone.getPrice().toString()),
					true, jsonPath(RETURNED).isNumber());

			String content = result.andReturn().getResponse().getContentAsString();
			
			Object data = objectMapper.readValue(new JSONObject(content).get(RETURNED).toString(), Integer.class);
				
			this.idsToDelete[0] = (Integer) data;
			
			result = super.test(
					post("/product/add").contentType("application/json").content(this.objectMapper.writeValueAsString(iPhone)),
					true, jsonPath(RETURNED).isNumber());
			
			content = result.andReturn().getResponse().getContentAsString();
			
			data = objectMapper.readValue(new JSONObject(content).get(RETURNED).toString(), Integer.class);
				
			this.idsToDelete[1] = (Integer) data;
			
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
			this.mockMvc.perform(get("/product/remove").param("id", this.idsToDelete[0].toString()))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath(SUCCESS).value(true));
			
			this.mockMvc.perform(get("/product/remove").param("id", this.idsToDelete[1].toString()))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath(SUCCESS).value(true));
			
			this.mockMvc.perform(get("/product/remove").param("id", "-1"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath(SUCCESS).value(false));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}