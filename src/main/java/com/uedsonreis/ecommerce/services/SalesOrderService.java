package com.uedsonreis.ecommerce.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uedsonreis.ecommerce.entities.Customer;
import com.uedsonreis.ecommerce.entities.Item;
import com.uedsonreis.ecommerce.entities.Product;
import com.uedsonreis.ecommerce.entities.SalesOrder;
import com.uedsonreis.ecommerce.entities.User;
import com.uedsonreis.ecommerce.repositories.ItemRepository;
import com.uedsonreis.ecommerce.repositories.SalesOrderRepository;
import com.uedsonreis.ecommerce.utils.Util;

@Service
public class SalesOrderService {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CustomerService customerService;

	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private SalesOrderRepository salesOrderRepository;
	
	@Transactional
	public SalesOrder invoice(Map<Integer, Item> cart, User user) throws Exception {
		Customer customer = this.customerService.get(user);
		if (customer == null) {
			throw new Exception(Util.getMsgCustomerDoesntExists());
		}
		
		for (Integer productId: cart.keySet()) {
			Product product = this.productService.get(productId);
			if (product == null) {
				throw new Exception(Util.getMsgProductIdDoesntExist());
			}
			
			Item item = cart.get(productId);
			
			if (product.getAmount() < item.getAmount()) {
				throw new Exception(Util.getMsgProductDoesntHaveAmount(product.getName()));
			}
			
			item.setProduct(product);
		}
		
		Double totalValue = 0.0;
		
		SalesOrder salesOrder = SalesOrder.builder().customer(customer).build();		
		this.salesOrderRepository.save(salesOrder);
		
		for (Item item: cart.values()) {
			Product product = item.getProduct();
			product.setAmount( product.getAmount() - item.getAmount() );
			
			totalValue += item.getPrice() * item.getAmount();
			
			salesOrder.setTotalValue(totalValue);
			
			item.setSalesOrder(salesOrder);
			this.itemRepository.save(item);
		}
		
		return salesOrder;
	}
	
	public Set<Item> getItems(Integer salesOrderId) {
		SalesOrder salesOrder = SalesOrder.builder().id(salesOrderId).build();
		return this.itemRepository.findAllBySalesOrder(salesOrder);
	}

	@Transactional
	public void remove(Integer id) {
		SalesOrder salesOrder = SalesOrder.builder().id(id).build();
		Set<Item> items = this.itemRepository.findAllBySalesOrder(salesOrder);
		
		this.itemRepository.deleteAll(items);
		this.salesOrderRepository.deleteById(id);
	}
	
	public Collection<SalesOrder> getSalesOrders(User user) {
		Customer customer = this.customerService.get(user);
		if (customer == null) return new ArrayList<SalesOrder>();
		return this.salesOrderRepository.findAllByCustomer(customer);
	}
	
}