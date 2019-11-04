package com.uedsonreis.ecommerce.entities;

import java.util.Date;
import java.util.Set;

public class SalesOrder {

	private Customer customer;
	private Set<Item> items;
	private Date createdAt = new Date();
	
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public Set<Item> getItems() {
		return items;
	}
	public void setItems(Set<Item> items) {
		this.items = items;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	@Override
	public String toString() {
		return "SalesOrder [customer=" + customer + ", items=" + items + ", createdAt=" + createdAt + "]";
	}

}