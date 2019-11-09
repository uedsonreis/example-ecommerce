package com.uedsonreis.ecommerce.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.uedsonreis.ecommerce.entities.Factory;
import com.uedsonreis.ecommerce.entities.Item;
import com.uedsonreis.ecommerce.entities.Product;
import com.uedsonreis.ecommerce.entities.SalesOrder;
import com.uedsonreis.ecommerce.entities.User;
import com.uedsonreis.ecommerce.repositories.CustomerRepository;
import com.uedsonreis.ecommerce.services.ProductService;
import com.uedsonreis.ecommerce.services.SalesOrderService;

@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestSalesControllers extends ControllerTester {

	@Autowired
	private ObjectMapper objectMapper;
	
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
		apple.setName("Apple Inc.");
		
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
		
		this.idsToDelete[0] = this.productService.save(macBook);
		this.idsToDelete[1] = this.productService.save(iPhone);
	}
	
	@AfterAll
	public void removeSameProducts() {
		this.productService.remove(this.idsToDelete[0]);
		this.productService.remove(this.idsToDelete[1]);
		this.salesOrderService.remove(this.idsToDelete[3]);
		this.customerRepository.deleteById(this.idsToDelete[2]);
	}
	
	private void testInvoice() throws Exception {
		
		User user = new User();
		user.setLogin("uedson@reis.com");
		user.setPassword("321");
		
		Customer customer = new Customer();
		customer.setAge(37);
		customer.setUser(user);
		customer.setName("Uedson Reis");
		customer.setEmail(user.getLogin());
		customer.setAddress("Rua Fulano de Tal, n. 13");
		
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
		this.idsToDelete[2] = (Integer) data;
		
		result = super.test(post("/sales/order/invoice"), true, jsonPath(RETURNED).exists());
		
		content = result.andReturn().getResponse().getContentAsString();
		data = objectMapper.readValue(new JSONObject(content).get(RETURNED).toString(), SalesOrder.class);
		this.idsToDelete[3] = ((SalesOrder) data).getId();
	}
	
	private void testAddInCart() throws Exception {
		ResultActions result = this.mockMvc.perform(get("/product/list"))
			.andDo(print()).andExpect(status().isOk())
			.andExpect(jsonPath(SUCCESS).value(true))
			.andExpect(jsonPath(RETURNED).isArray());
		
		String content = result.andReturn().getResponse().getContentAsString();
		
		Object data = objectMapper.readValue(new JSONObject(content).get(RETURNED).toString(), Product[].class);
			
		Product[] products = (Product[]) data;
		
		Product product1 = products[0];
		
		super.test(
				get("/cart/add")
					.param("productId", product1.getId().toString())
					.param("amount", "1")
					.param("price", product1.getPrice().toString()),
				true, jsonPath(RETURNED).doesNotExist());
		
		Product product2 = products[1];
		
		Item item = new Item();
		item.setAmount(2);
		item.setProduct(product2);
		item.setPrice(product2.getPrice() * 0.9);

		super.test(
				post("/cart/add").contentType("application/json").content(this.objectMapper.writeValueAsString(item)),
				true, jsonPath(RETURNED).doesNotExist());			
	}
	
	@Test
	public void testCart() {
		try {
			super.test(get("/cart/list"), false, jsonPath(RETURNED).doesNotExist());
			this.testAddInCart();
			super.test(get("/cart/list"), true, jsonPath(RETURNED).isArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSalesOrder() {
		try {
			super.test(get("/sales/order/list"), false, jsonPath(RETURNED).doesNotExist());
			this.testInvoice();
			super.test(get("/sales/order/list"), true, jsonPath(RETURNED).isArray());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}