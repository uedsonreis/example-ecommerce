package com.uedsonreis.ecommerce.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

import com.uedsonreis.ecommerce.entities.Customer;
import com.uedsonreis.ecommerce.entities.Factory;
import com.uedsonreis.ecommerce.entities.Item;
import com.uedsonreis.ecommerce.entities.Product;
import com.uedsonreis.ecommerce.entities.SalesOrder;
import com.uedsonreis.ecommerce.entities.User;
import com.uedsonreis.ecommerce.repositories.CustomerRepository;
import com.uedsonreis.ecommerce.repositories.ItemRepository;
import com.uedsonreis.ecommerce.repositories.ProductRepository;
import com.uedsonreis.ecommerce.repositories.SalesOrderRepository;

@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestSalesOrderService {
	
	private final User user = new User();
	private final Product product = new Product();
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private SalesOrderRepository salesOrderRepository;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private SalesOrderService salesOrderService;
	
	@BeforeAll
	public void prepareProduct() {
		Factory microsoft = new Factory();
		microsoft.setName("Microsoft");
		
		this.product.setAmount(5);
		this.product.setName("Microsoft Notebook");
		this.product.setPrice(6399.0);
		this.product.setFactory(microsoft);
		
		try {
			this.productService.save(this.product);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@BeforeAll
	public void prepareCustomer() {
		this.user.setLogin("fulano@tal.com");
		this.user.setPassword("123");
		
		Customer customer = new Customer();
		customer.setAge(40);
		customer.setUser(this.user);
		customer.setName("Fulano de Tal");
		customer.setEmail(this.user.getLogin());
		customer.setAddress("Rua Profº. Cicrano, n. 59, ap. 88");
		
		try {
			this.customerService.save(customer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@AfterAll
	public void clearDatabase() {
		this.itemRepository.deleteAll();
		this.salesOrderRepository.deleteAll();
		this.customerRepository.deleteAll();
		this.productRepository.deleteAll();
	}
	
	private void testNotList() {
		Collection<SalesOrder> sales = this.salesOrderService.getSalesOrders(this.user);
		
		assertEquals(true, sales != null);
		assertEquals(0, sales.size());
	}
	
	private void testInvoice() {
		Set<Item> cart = new HashSet<>();
		
		Item item = new Item();
		item.setProduct(this.product);
		item.setPrice(product.getPrice());
		item.setAmount(10);
		cart.add(item);
		
		SalesOrder salesOrder = null;
		try {
			salesOrder = this.salesOrderService.invoice(cart, this.user);
		} catch (Exception e) {
			// e.printStackTrace(); If catch here is because have passed in the test.
		}
		assertEquals(true, salesOrder == null);
		
		item.setAmount(1);
		
		try {
			salesOrder = this.salesOrderService.invoice(cart, this.user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(true, salesOrder != null);
	}
	
	private void testAmountProduct() {
		try {
			Product productDB = this.productRepository.findById(this.product.getId()).get();
			assertEquals(true, 4 == productDB.getAmount());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void testList() {
		Collection<SalesOrder> sales = this.salesOrderService.getSalesOrders(this.user);
		
		assertEquals(true, sales != null);
		assertEquals(true, sales.size() > 0);
	}
	
	@Test
	public void testAll() {
		this.testNotList();
		this.testInvoice();
		this.testList();
		this.testAmountProduct();
	}
	
}