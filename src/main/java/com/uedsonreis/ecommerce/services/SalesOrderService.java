package com.uedsonreis.ecommerce.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.uedsonreis.ecommerce.entities.SalesOrder;

@Service
public class SalesOrderService {

	private final List<SalesOrder> repository = new ArrayList<>();
	
	public Integer save(SalesOrder salesOrder) throws Exception {
		
		if (this.repository.add(salesOrder)) {
			return this.repository.indexOf(salesOrder);
		}
		
		return null;
	}
	
	public Collection<SalesOrder> getSalesOrders() {
		return this.repository;
	}
	
}