package com.uedsonreis.ecommerce.controllers;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;

import org.json.JSONArray;
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
import com.uedsonreis.ecommerce.api.dto.customer.CustomerInput;
import com.uedsonreis.ecommerce.entities.Customer;
import com.uedsonreis.ecommerce.entities.Factory;
import com.uedsonreis.ecommerce.entities.Item;
import com.uedsonreis.ecommerce.entities.Product;
import com.uedsonreis.ecommerce.entities.SalesOrder;
import com.uedsonreis.ecommerce.entities.User;
import com.uedsonreis.ecommerce.repositories.CustomerRepository;
import com.uedsonreis.ecommerce.repositories.ItemRepository;
import com.uedsonreis.ecommerce.repositories.UserRepository;
import com.uedsonreis.ecommerce.services.ProductService;
import com.uedsonreis.ecommerce.services.SalesOrderService;
import com.uedsonreis.ecommerce.utils.Util;

@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestSalesControllers extends ControllerTester {

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private SalesOrderService salesOrderService;
	
	@Autowired
	private ProductService productService;
	
	private Integer idsToDelete[] = new Integer[4];

	@BeforeAll
	public void setup(WebApplicationContext wac) {
		super.setup(wac);
	}
	
	@BeforeAll
	public void addSomeProducts() {
		Factory apple = new Factory();
		apple.setName("Apple");
		
		Product macBook = new Product();
		macBook.setAmount(5);
		macBook.setName("MackBook Pro");
		macBook.setPrice(14399.0);
		macBook.setFactory(apple);
		
		Product iPhone = new Product();
		iPhone.setAmount(10);
		iPhone.setName("iPhone 11");
		iPhone.setPrice(4399.0);
		iPhone.setFactory(apple);
		
		try {
			this.idsToDelete[0] = this.productService.save(macBook);
			this.idsToDelete[1] = this.productService.save(iPhone);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@AfterAll
	public void removeSameProducts() {
		this.itemRepository.deleteAll();
		this.salesOrderService.remove(this.idsToDelete[2]);
		this.productService.remove(this.idsToDelete[0]);
		this.productService.remove(this.idsToDelete[1]);
		
		for (Customer c: this.customerRepository.findAll()) {
			this.customerRepository.delete(c);
			this.userRepository.delete(c.getUser());
		}
	}
	
	private void testAddInCart() throws Exception {
		ResultActions result = this.mockMvc.perform(get("/product/list"))
			.andDo(print()).andExpect(status().isOk());
		
		String content = result.andReturn().getResponse().getContentAsString();
		
		Object data = objectMapper.readValue(new JSONArray(content).toString(), Product[].class);
			
		Product[] products = (Product[]) data;
		
		Product product1 = products[0];
		
		Item item = new Item();
		item.setAmount(1);
		item.setProduct(product1);
		item.setPrice(product1.getPrice());
		
		super.test(
				post("/cart/add").contentType("application/json").content(this.objectMapper.writeValueAsString(item)),
				status().isOk());
		
		Product product2 = products[1];
		
		item = new Item();
		item.setAmount(2);
		item.setProduct(product2);
		item.setPrice(product2.getPrice() * 0.9);

		super.test(
				post("/cart/add").contentType("application/json").content(this.objectMapper.writeValueAsString(item)),
				status().isOk());
	}
	
	@Test
	public void testCart() {
		try {
			super.test(get("/cart/list"), status().isNoContent());
			this.testAddInCart();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSalesOrder() {
		try {
			super.test(get("/sales/order/list"), status().isUnauthorized());
			
			User user = new User();
			user.setLogin("uedson@reis.com");
			user.setPassword("321");
			
			CustomerInput customer = new CustomerInput();
			customer.setAge(37);
			customer.setUserPassword(user.getPassword());
			customer.setName("Uedson Reis");
			customer.setEmail(user.getLogin());
			customer.setAddress("Rua Fulano de Tal, n. 13");
			
			ResultActions result = super.test(
					post("/user/customer/add").contentType("application/json").content(this.objectMapper.writeValueAsString(customer)),
					status().isCreated());
			
			String token = result.andReturn().getResponse().getContentAsString();
			assertNotEquals("", token);
			
			result = super.test(get("/cart/list"), status().isOk());
			String content = result.andReturn().getResponse().getContentAsString();
			Object data = objectMapper.readValue(new JSONArray(content).toString(), Collection.class);
			
			result = super.test(
					post("/sales/order/invoice")
						.contentType("application/json").content(this.objectMapper.writeValueAsString(data))
						.header(Util.AUTH, this.treatToken(token)),
					status().isOk());

			content = result.andReturn().getResponse().getContentAsString();
			assertNotEquals("", content);
			
			data = objectMapper.readValue(new JSONObject(content).toString(), SalesOrder.class);
			this.idsToDelete[2] = ((SalesOrder) data).getId();
			
			super.test(get("/sales/order/list").header(Util.AUTH, this.treatToken(token)), status().isOk());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}