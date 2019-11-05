package com.uedsonreis.ecommerce.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uedsonreis.ecommerce.entities.Customer;
import com.uedsonreis.ecommerce.entities.Item;
import com.uedsonreis.ecommerce.entities.Product;
import com.uedsonreis.ecommerce.entities.SalesOrder;

@Service
public class SalesOrderService {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CustomerService customerService;

	private final List<SalesOrder> repository = new ArrayList<>();
	
	public Integer invoice(Map<Integer, Item> cart, Integer customerId) {
		for (Integer productId: cart.keySet()) {
			Product product = this.productService.get(productId);
			if (product == null) return null;
			
			Item item = cart.get(productId);
			item.setProduct(product);
		}
		
		Customer customer = this.customerService.get(customerId);
		if (customer == null) return null;
		
		SalesOrder salesOrder = new SalesOrder();
		salesOrder.setItems(new HashSet<>(cart.values()));
		salesOrder.setCustomer(customer);
		
		return this.save(salesOrder);
	}
	
	private Integer save(SalesOrder salesOrder) {
		
		if (this.repository.add(salesOrder)) {
			return this.repository.indexOf(salesOrder);
		}
		
		return null;
	}
	
	public Collection<SalesOrder> getSalesOrders() {
		return this.repository;
	}
	
}