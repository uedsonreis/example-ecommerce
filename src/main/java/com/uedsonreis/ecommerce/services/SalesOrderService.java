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
import com.uedsonreis.ecommerce.entities.User;

@Service
public class SalesOrderService {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CustomerService customerService;

	private final List<SalesOrder> repository = new ArrayList<>();
	
	public SalesOrder invoice(Map<Integer, Item> cart, User user) {
		Double totalValue = 0.0;
		
		Customer customer = this.customerService.get(user);
		if (customer == null) return null;
		
		for (Integer productId: cart.keySet()) {
			Product product = this.productService.get(productId);
			if (product == null) return null;
			
			Item item = cart.get(productId);
			
			if (product.getAmount() < item.getAmount()) return null;
			
			item.setProduct(product);
		}
		
		for (Item item: cart.values()) {
			Product product = item.getProduct();
			
			product.setAmount( product.getAmount() - item.getAmount() );
			totalValue += item.getPrice() * item.getAmount();
			
			this.productService.save(product);
		}
		
		SalesOrder salesOrder = new SalesOrder();
		salesOrder.setItems(new HashSet<>(cart.values()));
		salesOrder.setCustomer(customer);
		salesOrder.setTotalValue(totalValue);
		
		this.repository.add(salesOrder);
		salesOrder.setId( this.repository.size() - 1 );
		
		return salesOrder;
	}
	
	public Collection<SalesOrder> getSalesOrders(User user) {
		Collection<SalesOrder> result = new ArrayList<>();
		
		for (SalesOrder order: this.repository) {
			if (order.getCustomer().getUser().equals(user)) {
				result.add(order);
			}
		}
		
		return result;
	}
	
}